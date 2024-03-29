package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.redis.NotificationEntity;
import com.sahin.library_management.infra.enums.NotificationType;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.notification.Notification;
import com.sahin.library_management.mapper.NotificationMapper;
import com.sahin.library_management.repository.redis.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class NotificationService {

    private static final String LOAN_ID_PREFIX = "loan-";
    private static final String TIME_TO_SEND_SUFFIX = ":timeToSend";
    private String timeToSendKey;
    private String redisHashValue;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${spring.activemq.queue.name}")
    private String activemqQueueName;

    @PostConstruct
    void init() {
        redisHashValue = NotificationEntity.class.getAnnotation(RedisHash.class).value();
        timeToSendKey = redisHashValue + TIME_TO_SEND_SUFFIX;
    }

    public void createLoanNotifications(BookLoaning loaning) {

        List<NotificationEntity> entities = new ArrayList<>();
        entities.addAll(createLoanNotificationsBeforeDueDate(loaning));
        entities.add(createLoanNotificationForToday(loaning));
        entities.addAll(createLoanNotificationsAfterDueDate(loaning));

        notificationRepository.saveAll(entities);
        redisTemplate.opsForZSet().add(timeToSendKey, getTypedTuples(entities));
    }

    public void deleteLoanNotifications(BookLoaning loaning) {

        List<NotificationEntity> entities = notificationRepository.findAllByAboutId(LOAN_ID_PREFIX + loaning.getId());
        notificationRepository.deleteAll(entities);

        if (!entities.isEmpty())
            redisTemplate.opsForZSet().remove(timeToSendKey, entities.stream()
                    .map(NotificationEntity::getId)
                    .toArray());
    }

    @Scheduled(fixedDelay = 10000)
    public void processNotifications() {

        long currentTime = Instant.now().toEpochMilli();

        List<NotificationEntity> entities = getNotificationsBeforeTime(currentTime);

        List<Notification> notifications = notificationMapper.toModels(entities);
        sendToQueue(notifications);

        deleteFromCache(entities, currentTime);
    }

    private List<NotificationEntity> createLoanNotificationsBeforeDueDate(BookLoaning loaning) {

        Instant dueDateInstant = Instant.ofEpochMilli(loaning.getDueDate());

        Map<Long, Long> dueDateAmounts = new HashMap<>();
        dueDateAmounts.put(3L, dueDateInstant.minus(3L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(2L, dueDateInstant.minus(2L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(1L, dueDateInstant.minus(1L, ChronoUnit.DAYS).toEpochMilli());

        List<NotificationEntity> entities = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : dueDateAmounts.entrySet()) {

            NotificationEntity entity = new NotificationEntity();
            entity.setCardBarcode(loaning.getMember().getLibraryCard().getBarcode());
            entity.setMail(loaning.getMember().getEmail());
            entity.setType(NotificationType.RETURN_TIME_CLOSING);
            entity.setAboutId(LOAN_ID_PREFIX + loaning.getId().toString());
            entity.setTimeToSend(entry.getValue());
            entity.setContent(entity.getType().getContent(entry.getKey().toString() + " day(s) ",
                    loaning.getMember().getLibraryCard().getBarcode()));

            entities.add(entity);
        }

        return entities;
    }

    private List<NotificationEntity> createLoanNotificationsAfterDueDate(BookLoaning loaning) {

        Instant dueDateInstant = Instant.ofEpochMilli(loaning.getDueDate());

        Map<Long, Long> dueDateAmounts = new HashMap<>();
        dueDateAmounts.put(3L, dueDateInstant.plus(3L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(2L, dueDateInstant.plus(2L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(1L, dueDateInstant.plus(1L, ChronoUnit.DAYS).toEpochMilli());

        List<NotificationEntity> entities = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : dueDateAmounts.entrySet()) {

            NotificationEntity entity = new NotificationEntity();
            entity.setCardBarcode(loaning.getMember().getLibraryCard().getBarcode());
            entity.setMail(loaning.getMember().getEmail());
            entity.setType(NotificationType.RETURN_TIME_PASSED);
            entity.setAboutId(LOAN_ID_PREFIX + loaning.getId().toString());
            entity.setTimeToSend(entry.getValue());
            entity.setContent(entity.getType().getContent(entry.getKey().toString() + " day(s) ",
                    loaning.getMember().getLibraryCard().getBarcode()));

            entities.add(entity);
        }

        return entities;
    }

    private NotificationEntity createLoanNotificationForToday(BookLoaning loaning) {

        NotificationEntity entity = new NotificationEntity();
        entity.setCardBarcode(loaning.getMember().getLibraryCard().getBarcode());
        entity.setMail(loaning.getMember().getEmail());
        entity.setType(NotificationType.RETURN_TIME_TODAY);
        entity.setAboutId(LOAN_ID_PREFIX + loaning.getId().toString());
        entity.setTimeToSend(loaning.getDueDate());
        entity.setContent(entity.getType().getContent(loaning.getMember().getLibraryCard().getBarcode()));

        return entity;
    }


    private Set<ZSetOperations.TypedTuple<String>> getTypedTuples(List<NotificationEntity> entities) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        entities.forEach(
                entity -> typedTuples.add(new DefaultTypedTuple<>(entity.getId(), entity.getTimeToSend().doubleValue()))
        );
        return typedTuples;
    }

    private List<NotificationEntity> getNotificationsBeforeTime(long time) {

        Set<String> keys = redisTemplate.opsForZSet().rangeByScore(timeToSendKey, Double.MIN_VALUE, time);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        List<NotificationEntity> entities = new ArrayList<>();

        if (keys != null) {
            for (String key : keys) {

                Map<String, String> entityMap = hashOperations.entries(redisHashValue + ":" + key);

                if (!entityMap.isEmpty()) {
                    NotificationEntity entity = notificationMapper.toEntity(entityMap);
                    entities.add(entity);

                    log.info("Getting notification with id " + entity.getId());
                }
            }
        }

        return entities;
    }


    private void deleteFromCache(List<NotificationEntity> entities, long time) {

        notificationRepository.deleteAll(entities);
        redisTemplate.opsForZSet().removeRangeByScore(timeToSendKey, Double.MIN_VALUE, time);
        log.info(entities.size() + " items has deleted from cache");
    }

    private void sendToQueue(List<Notification> notifications) {
        notifications.forEach(entity -> jmsTemplate.convertAndSend(activemqQueueName, entity));
        log.info(notifications.size() + " items has pushed to the queue");
    }
}

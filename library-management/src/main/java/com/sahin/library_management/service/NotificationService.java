package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.NotificationEntity;
import com.sahin.library_management.infra.enums.NotificationType;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class NotificationService {

    private static final String LOAN_ID_PREFIX = "loan-";
    private static final String TIME_TO_SEND_SUFFIX = ":timeToSend";
    private String timeToSendKey;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    void init() {
        timeToSendKey = NotificationEntity.class.getAnnotation(RedisHash.class).value() + TIME_TO_SEND_SUFFIX;
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

        redisTemplate.opsForZSet().remove(timeToSendKey, getTypedTuples(entities));
    }

    @Scheduled(fixedDelay = 10000)
    public void getAllNotifications() {

        Long currentTime = Instant.now().toEpochMilli();

        Set<String> keys = redisTemplate.opsForZSet().rangeByScore(timeToSendKey, Double.MIN_VALUE, currentTime);
        List<Object> entities = redisTemplate.boundHashOps("notifications:35a94ce5-c063-4eec-a832-9e3d0511d7e5").values();

//        List<NotificationEntity> entities = notificationRepository.findAllByIdIn(keys);

        entities.forEach(System.out::println);
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
        entity.setType(NotificationType.RETURN_TIME_TODAY);
        entity.setAboutId(LOAN_ID_PREFIX + loaning.getId().toString());
        entity.setTimeToSend(loaning.getDueDate());
        entity.setContent(entity.getType().getContent(loaning.getMember().getLibraryCard().getBarcode()));

        return entity;
    }


    private Set<ZSetOperations.TypedTuple<String>> getTypedTuples(List<NotificationEntity> entities) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        entities.forEach(
                entity -> typedTuples.add(new DefaultTypedTuple<String>(entity.getId(), entity.getTimeToSend().doubleValue()))
        );
        return typedTuples;
    }
}

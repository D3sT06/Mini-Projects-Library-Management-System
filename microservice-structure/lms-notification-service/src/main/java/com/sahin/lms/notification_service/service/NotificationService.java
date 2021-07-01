package com.sahin.lms.notification_service.service;

import com.sahin.lms.infra.entity.redis.NotificationEntity;
import com.sahin.lms.infra.mapper.NotificationMapper;
import com.sahin.lms.infra.model.notification.BaseNotification;
import com.sahin.lms.infra.model.notification.Notification;
import com.sahin.lms.notification_service.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class NotificationService {

    protected NotificationRepository notificationRepository;
    protected RedisTemplate<String, String> redisTemplate;
    protected NotificationMapper notificationMapper;
    protected JmsTemplate jmsTemplate;
    protected String redisHashValue;
    protected String timeToSendKey;

    private static final String TIME_TO_SEND_SUFFIX = ":timeToSend";

    @Value("${spring.activemq.queue.notifications}")
    protected String notificationsQueueName;

    @Value("${spring.activemq.queue.mail}")
    private String mailQueueName;

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setNotificationMapper(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    protected void init() {
        redisHashValue = NotificationEntity.class.getAnnotation(RedisHash.class).value();
        timeToSendKey = redisHashValue + TIME_TO_SEND_SUFFIX;
    }

    @Scheduled(fixedDelay = 10000)
    public void processNotifications() {

        long currentTime = Instant.now().toEpochMilli();
        List<NotificationEntity> entities = getNotificationsBeforeTime(currentTime);
        List<Notification> notifications = notificationMapper.toModels(entities);
        sendToQueue(notifications);
        deleteFromCache(entities, currentTime);
    }

    @JmsListener(destination = "${spring.activemq.queue.notificationsQueueName}")
    public void receive(BaseNotification notification) {

        switch (notification.getEvent()) {
            case LOAN_CREATE:
            case LOAN_DELETE:
                handleLoanNotifications(notification);
                break;
            default:
                break;
        }
    }

    protected abstract void handleLoanNotifications(BaseNotification notification);

    private void sendToQueue(List<Notification> notifications) {
        notifications.forEach(entity -> jmsTemplate.convertAndSend(mailQueueName, entity));
        log.info(notifications.size() + " items has pushed to the queue");
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
}

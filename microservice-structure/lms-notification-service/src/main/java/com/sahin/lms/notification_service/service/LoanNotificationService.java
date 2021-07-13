package com.sahin.lms.notification_service.service;

import com.sahin.lms.infra.entity.notification.redis.NotificationEntity;
import com.sahin.lms.infra.enums.NotificationType;
import com.sahin.lms.infra.model.notification.BaseNotification;
import com.sahin.lms.infra.model.notification.LoanNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class LoanNotificationService extends NotificationService {

    private static final String LOAN_ID_PREFIX = "loan-";

    @PostConstruct
    public void init() {
        super.init();
    }

    public void handleLoanNotifications(BaseNotification notification) {

        log.info("Book loaning notification has pulled. Loan id: " + ((LoanNotification) notification).getBookLoaningId());

        switch (notification.getEvent()) {
            case LOAN_CREATE:
                createLoanNotifications((LoanNotification) notification);
                break;
            case LOAN_DELETE:
                deleteLoanNotifications((LoanNotification) notification);
                break;
            default:
                break;
        }
    }

    private void createLoanNotifications(LoanNotification notification) {

        List<NotificationEntity> entities = new ArrayList<>();
        entities.addAll(createLoanNotificationsBeforeDueDate(notification));
        entities.add(createLoanNotificationForToday(notification));
        entities.addAll(createLoanNotificationsAfterDueDate(notification));

        notificationRepository.saveAll(entities);
        redisTemplate.opsForZSet().add(timeToSendKey, getTypedTuples(entities));
        log.info(entities.size() + " notifications were created.");
    }

    private void deleteLoanNotifications(LoanNotification notification) {

        List<NotificationEntity> entities = notificationRepository.findAllByAboutId(LOAN_ID_PREFIX + notification.getBookLoaningId());
        notificationRepository.deleteAll(entities);

        if (!entities.isEmpty())
            redisTemplate.opsForZSet().remove(timeToSendKey, entities.stream()
                    .map(NotificationEntity::getId)
                    .toArray());

        log.info(entities.size() + " notifications were deleted.");
    }

    private List<NotificationEntity> createLoanNotificationsBeforeDueDate(LoanNotification notification) {

        Instant dueDateInstant = Instant.ofEpochMilli(notification.getBookLoaningDueDate());

        Map<Long, Long> dueDateAmounts = new HashMap<>();
        dueDateAmounts.put(3L, dueDateInstant.minus(3L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(2L, dueDateInstant.minus(2L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(1L, dueDateInstant.minus(1L, ChronoUnit.DAYS).toEpochMilli());

        List<NotificationEntity> entities = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : dueDateAmounts.entrySet()) {

            NotificationEntity entity = new NotificationEntity();
            entity.setMemberId(notification.getMemberId());
            entity.setMail(notification.getMail());
            entity.setType(NotificationType.RETURN_TIME_CLOSING);
            entity.setAboutId(LOAN_ID_PREFIX + notification.getBookLoaningId().toString());
            entity.setTimeToSend(entry.getValue());
            entity.setContent(entity.getType().getContent(entry.getKey().toString() + " day(s) ",
                    notification.getMemberId().toString()));

            entities.add(entity);
        }

        return entities;
    }

    private List<NotificationEntity> createLoanNotificationsAfterDueDate(LoanNotification notification) {

        Instant dueDateInstant = Instant.ofEpochMilli(notification.getBookLoaningDueDate());

        Map<Long, Long> dueDateAmounts = new HashMap<>();
        dueDateAmounts.put(3L, dueDateInstant.plus(3L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(2L, dueDateInstant.plus(2L, ChronoUnit.DAYS).toEpochMilli());
        dueDateAmounts.put(1L, dueDateInstant.plus(1L, ChronoUnit.DAYS).toEpochMilli());

        List<NotificationEntity> entities = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : dueDateAmounts.entrySet()) {

            NotificationEntity entity = new NotificationEntity();
            entity.setMemberId(notification.getMemberId());
            entity.setMail(notification.getMail());
            entity.setType(NotificationType.RETURN_TIME_PASSED);
            entity.setAboutId(LOAN_ID_PREFIX + notification.getBookLoaningId().toString());
            entity.setTimeToSend(entry.getValue());
            entity.setContent(entity.getType().getContent(entry.getKey().toString() + " day(s) ",
                    notification.getMemberId().toString()));

            entities.add(entity);
        }

        return entities;
    }

    private NotificationEntity createLoanNotificationForToday(LoanNotification notification) {

        NotificationEntity entity = new NotificationEntity();
        entity.setMemberId(notification.getMemberId());
        entity.setMail(notification.getMail());
        entity.setType(NotificationType.RETURN_TIME_TODAY);
        entity.setAboutId(LOAN_ID_PREFIX + notification.getBookLoaningId().toString());
        entity.setTimeToSend(notification.getBookLoaningDueDate());
        entity.setContent(entity.getType().getContent(notification.getMemberId().toString()));

        return entity;
    }

    private Set<ZSetOperations.TypedTuple<String>> getTypedTuples(List<NotificationEntity> entities) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        entities.forEach(
                entity -> typedTuples.add(new DefaultTypedTuple<>(entity.getId(), entity.getTimeToSend().doubleValue()))
        );
        return typedTuples;
    }
}

package com.sahin.lms.notification_service.repository;


import com.sahin.lms.infra.entity.notification.redis.NotificationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByAboutId(String aboutId);
}

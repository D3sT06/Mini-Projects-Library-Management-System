package com.sahin.lms.notification_service.repository;


import com.sahin.lms.infra.entity.redis.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByAboutId(String aboutId);
}

package com.sahin.library_management.repository.redis;


import com.sahin.library_management.infra.entity.redis.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByAboutId(String aboutId);
}

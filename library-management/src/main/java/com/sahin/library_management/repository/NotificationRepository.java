package com.sahin.library_management.repository;


import com.sahin.library_management.infra.entity.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByAboutId(String aboutId);
}

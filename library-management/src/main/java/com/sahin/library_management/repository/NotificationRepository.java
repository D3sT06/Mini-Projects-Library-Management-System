package com.sahin.library_management.repository;


import com.sahin.library_management.infra.entity.NotificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface NotificationRepository extends CrudRepository<NotificationEntity, String> {

    List<NotificationEntity> findAllByIdIn(Set<String> ids);
    List<NotificationEntity> findAllByAboutId(String aboutId);
}

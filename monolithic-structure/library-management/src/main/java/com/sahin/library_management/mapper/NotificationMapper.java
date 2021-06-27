package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.redis.NotificationEntity;
import com.sahin.library_management.infra.enums.NotificationType;
import com.sahin.library_management.infra.model.notification.Notification;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Logger log = LoggerFactory.getLogger(NotificationMapper.class);

    NotificationEntity toEntity(Map<String, String> map);

    @AfterMapping
    default void doMappings(Map<String, String> map, @MappingTarget NotificationEntity entity) {

        for (String key : map.keySet()) {
            try {
                if (key.equals("_class"))
                    continue;

                Field field = NotificationEntity.class.getDeclaredField(key);
                field.setAccessible(true);
                if (field.getType().equals(String.class)) {
                    field.set(entity, map.get(key));
                }
                else if (field.getType().equals(Long.class)) {
                    field.set(entity, Long.parseLong(map.get(key)));
                }
                else if (field.getType().equals(NotificationType.class)) {
                    field.set(entity, NotificationType.valueOf(map.get(key)));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("Cannot map into notification entity", e);
            }
        }
    }

    Notification toModel(NotificationEntity entity);
    List<Notification> toModels(List<NotificationEntity> entities);
}

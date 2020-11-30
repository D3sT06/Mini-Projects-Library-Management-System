package com.sahin.library_management.infra.entity;

import com.sahin.library_management.infra.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash("notifications")
public class NotificationEntity {

    @Id
    private String id;

    @Indexed
    private String aboutId;

    private String cardBarcode;
    private NotificationType type;
    private Long timeToSend;

    private String content;
}

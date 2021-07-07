package com.sahin.lms.infra.entity.notification.redis;

import com.sahin.lms.infra.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Setter
@RedisHash("notifications")
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 568971231478502111L;

    @Id
    private String id;

    @Indexed
    private String aboutId;

    private String cardBarcode;
    private String mail;
    private NotificationType type;
    private Long timeToSend;

    private String content;
}

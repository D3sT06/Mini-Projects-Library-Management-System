package com.sahin.library_management.infra.model.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsNotification extends Notification {
    private String phone;
}

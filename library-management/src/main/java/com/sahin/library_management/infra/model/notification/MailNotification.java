package com.sahin.library_management.infra.model.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailNotification extends Notification {
    private String mail;
}

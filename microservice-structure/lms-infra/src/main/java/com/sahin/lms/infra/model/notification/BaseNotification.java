package com.sahin.lms.infra.model.notification;

import com.sahin.lms.infra.enums.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseNotification {

    protected NotificationEvent event;
    protected String cardBarcode;
    protected String mail;
}

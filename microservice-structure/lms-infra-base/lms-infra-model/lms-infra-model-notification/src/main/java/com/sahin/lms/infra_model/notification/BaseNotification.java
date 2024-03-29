package com.sahin.lms.infra_model.notification;

import com.sahin.lms.infra_enum.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseNotification implements Serializable {

    protected NotificationEvent event;
    protected Long memberId;
    protected String mail;
}

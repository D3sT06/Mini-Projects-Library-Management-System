package com.sahin.lms.infra_model.notification;

import com.sahin.lms.infra_enum.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Notification implements Serializable {

    private static final long serialVersionUID = 51112511102103398L;

    private String id;
    private String aboutId;
    private String cardBarcode;
    private String mail;
    private NotificationType type;
    private Long timeToSend;
    private String content;
}


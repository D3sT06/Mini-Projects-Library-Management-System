package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.NotificationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @JmsListener(destination = "${spring.activemq.queue.name}")
    public void receiveNotification(NotificationEntity notification) {
        log.info("Item has pulled. Id: " + notification.getId());
    }

}

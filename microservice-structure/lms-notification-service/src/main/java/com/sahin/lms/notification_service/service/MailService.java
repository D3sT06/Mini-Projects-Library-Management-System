package com.sahin.lms.notification_service.service;

import com.sahin.lms.infra.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMailAddress;

    @JmsListener(destination = "${spring.activemq.queue.mail}")
    public void receiveNotification(Notification notification) {

        log.info("Item has pulled. Id: " + notification.getId());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMailAddress);
        message.setTo(notification.getMail());
        message.setSubject(notification.getType().toString());
        message.setText(notification.getContent());
        mailSender.send(message);

        log.info("Mail has sent to " + notification.getMail());
    }

}

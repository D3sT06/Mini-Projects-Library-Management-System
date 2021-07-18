package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra_enum.NotificationEvent;
import com.sahin.lms.infra_model.loan.BookLoaning;
import com.sahin.lms.infra_model.notification.LoanNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${spring.activemq.queue.name}")
    private String activemqQueueName;

    public void deleteLoanNotifications(BookLoaning loaning, String mail) {
        sendToQueue(loaning, mail, NotificationEvent.LOAN_DELETE);
    }

    public void createLoanNotifications(BookLoaning loaning, String mail) {
        sendToQueue(loaning, mail, NotificationEvent.LOAN_CREATE);
    }

    private void sendToQueue(BookLoaning bookLoaning, String mail, NotificationEvent event) {
        LoanNotification loanNotification = new LoanNotification(
                bookLoaning.getId(),
                bookLoaning.getDueDate(),
                event,
                bookLoaning.getMemberId(),
                mail
        );

        jmsTemplate.convertAndSend(activemqQueueName, loanNotification);
        log.info("Loan notification has pushed to the queue");
    }
}

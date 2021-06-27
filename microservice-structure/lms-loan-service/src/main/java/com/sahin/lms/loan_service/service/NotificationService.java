package com.sahin.lms.loan_service.service;

import com.sahin.lms.infra.enums.NotificationEvent;
import com.sahin.lms.infra.model.book.BookLoaning;
import com.sahin.lms.loan_service.model.LoanNotification;
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

    public void deleteLoanNotifications(BookLoaning loaning) {
        sendToQueue(loaning.getId(), NotificationEvent.DELETE);
    }

    public void createLoanNotifications(BookLoaning loaning) {
        sendToQueue(loaning.getId(), NotificationEvent.CREATE);
    }

    public void sendToQueue(Long bookLoaningId, NotificationEvent event) {
        LoanNotification loanNotification = new LoanNotification(bookLoaningId, event);
        jmsTemplate.convertAndSend(activemqQueueName, loanNotification);
        log.info("Loan notification has pushed to the queue");
    }
}

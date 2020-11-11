package com.sahin.library_management.component;

import com.sahin.library_management.config.RabbitMqConfig;
import com.sahin.library_management.infra.model.log.MemberLog;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(RabbitMqConfig.class)
public class MemberEventsHandler {

    @RabbitListener(queues = {"books", "book_items", "loans", "reservations"})
    public void receiveMessages(MemberLog memberLog) {
        System.out.println(memberLog.toString());
    }
}

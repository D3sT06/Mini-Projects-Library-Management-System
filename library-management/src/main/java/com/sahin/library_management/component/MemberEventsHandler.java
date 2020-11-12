package com.sahin.library_management.component;

import com.sahin.library_management.config.RabbitMqConfig;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.service.MemberService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
@ConditionalOnBean(RabbitMqConfig.class)
public class MemberEventsHandler {

    @Autowired
    private MemberService memberService;

    private BlockingQueue<MemberLog> memberLogsQueue = new ArrayBlockingQueue<>(1000);

    @RabbitListener(queues = {"books", "book_items", "loans", "reservations"})
    public void receiveMessages(MemberLog memberLog) throws InterruptedException {
        memberLogsQueue.put(memberLog);
    }

    @Scheduled
    @Async
    public void saveLogs() {

        memberLogsQueue.removeA

    }
}

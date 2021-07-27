package com.sahin.library_management.component;

import com.sahin.library_management.config.RabbitMqConfig;
import com.sahin.library_management.infra.model.log.MemberLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
@ConditionalOnBean(RabbitMqConfig.class)
@Slf4j
public class MemberEventsHandler {

    private BlockingQueue<MemberLog> memberLogsQueue = new ArrayBlockingQueue<>(1000);

    @RabbitListener(queues = {"#{bookQueue.name}", "#{itemQueue.name}", "#{loanQueue.name}", "#{reservationQueue.name}"})
    public void receiveMessages(MemberLog memberLog) throws InterruptedException {
        memberLogsQueue.put(memberLog);
    }

    @Scheduled(fixedDelay = 10000)
    public void saveLogs() {
        log.info("Member logs queue size: " + memberLogsQueue.size());
    }
}

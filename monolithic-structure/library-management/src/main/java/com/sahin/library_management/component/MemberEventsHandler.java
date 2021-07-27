package com.sahin.library_management.component;

import com.sahin.library_management.config.RabbitMqConfig;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.service.member_log.MemberLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
@ConditionalOnBean(RabbitMqConfig.class)
@Slf4j
public class MemberEventsHandler {

    @Autowired
    private MemberLogService memberLogService;

    private BlockingQueue<MemberLog> memberLogsQueue = new ArrayBlockingQueue<>(1000);

    @RabbitListener(queues = {"#{bookQueue.name}", "#{itemQueue.name}", "#{loanQueue.name}", "#{reservationQueue.name}"})
    public void receiveMessages(MemberLog memberLog) throws InterruptedException {
        memberLogsQueue.put(memberLog);
    }

    @Scheduled(fixedDelay = 10000)
    public void saveLogs() {
        Collection<MemberLog> collection = new ArrayList<>(memberLogsQueue.size());

        memberLogsQueue.drainTo(collection);
        memberLogService.saveAll(collection);

        log.info(collection.size() + " items has been saved to the mongo db");
    }
}

package com.sahin.library_management.component;

import com.sahin.library_management.config.RabbitMqConfig;
import com.sahin.library_management.infra.entity.LogAggregationEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.infra.model.log.MemberLogWithBarcodeAggregation;
import com.sahin.library_management.service.LogAggregationService;
import com.sahin.library_management.service.member_log.MemberLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
@ConditionalOnBean(RabbitMqConfig.class)
@Slf4j
public class MemberEventsHandler {

    @Autowired
    private MemberLogService memberLogService;

    @Autowired
    private LogAggregationService logAggregationService;

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

    @Scheduled(fixedDelay = 10000)
    public void saveAggregations() {

        List<LogAggregationEntity> aggregations = new LinkedList<>();

        for(QueryTerm term : QueryTerm.values()) {
            List<MemberLogWithBarcodeAggregation> events = memberLogService.getActionAggregations(term);

            events.forEach(event -> {
                LogAggregationEntity logAggregationEntity = new LogAggregationEntity();
                logAggregationEntity.setAccountFor(AccountFor.MEMBER);
                logAggregationEntity.setAction(LogAction.valueOf(event.getGroup().getAction()));
                logAggregationEntity.setActionCount(event.getActionCount());
                logAggregationEntity.setBarcode(event.getGroup().getCardBarcode());
                logAggregationEntity.setQueryTerm(term);

                aggregations.add(logAggregationEntity);
            });

        }

        logAggregationService.saveAll(aggregations);

        log.info("Aggregations has been updated with " + aggregations.size() + " items");
    }
}

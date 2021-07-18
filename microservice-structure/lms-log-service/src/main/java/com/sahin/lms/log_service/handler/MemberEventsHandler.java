package com.sahin.lms.log_service.handler;

import com.sahin.lms.infra_enum.AccountFor;
import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.QueryTerm;
import com.sahin.lms.infra_model.log.LogAggregation;
import com.sahin.lms.infra_model.log.MemberLogWithBarcodeAggregation;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import com.sahin.lms.log_service.config.RabbitMqConfig;
import com.sahin.lms.log_service.service.LogAggregationService;
import com.sahin.lms.log_service.service.MemberLogService;
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

        List<LogAggregation> aggregations = new LinkedList<>();

        for(QueryTerm term : QueryTerm.values()) {
            List<MemberLogWithBarcodeAggregation> events = memberLogService.getActionAggregations(term);

            events.forEach(event -> {
                LogAggregation logAggregation = new LogAggregation();
                logAggregation.setAccountFor(AccountFor.MEMBER);
                logAggregation.setAction(LogAction.valueOf(event.getGroup().getAction()));
                logAggregation.setActionCount(event.getActionCount());
                logAggregation.setBarcode(event.getGroup().getCardBarcode());
                logAggregation.setQueryTerm(term);

                aggregations.add(logAggregation);
            });

        }

        logAggregationService.saveAll(aggregations);

        log.info("Aggregations has been updated with " + aggregations.size() + " items");
    }
}

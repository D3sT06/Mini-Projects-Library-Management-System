package com.sahin.library_management.service.member_log;

import com.sahin.library_management.factory.QueueFactory;
import com.sahin.library_management.infra.enums.LogTopic;
import com.sahin.library_management.infra.model.log.MemberLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ConcreteMemberLogPublisherService implements MemberLogPublisherService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private QueueFactory queueFactory;

    @Autowired
    private TopicExchange memberEventsTopicExchange;

    public void send(LogTopic logTopic, MemberLog memberLog) {

        try {
            String routingKey = queueFactory.get(logTopic).getName();
            rabbitTemplate.convertAndSend(memberEventsTopicExchange.getName(), routingKey, memberLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

package com.sahin.lms.infra.service.member_log;

import com.sahin.lms.infra.enums.LogTopic;
import com.sahin.lms.infra.factory.QueueFactory;
import com.sahin.lms.infra.model.log.MemberLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class ConcreteMemberLogPublisherService implements MemberLogPublisherService {

    private RabbitTemplate rabbitTemplate;
    private TopicExchange memberEventsTopicExchange;
    private QueueFactory queueFactory;

    public ConcreteMemberLogPublisherService(RabbitTemplate rabbitTemplate, TopicExchange topicExchange, QueueFactory queueFactory) {
        this.rabbitTemplate = rabbitTemplate;
        this.memberEventsTopicExchange = topicExchange;
        this.queueFactory = queueFactory;
    }

    public void send(LogTopic logTopic, MemberLog memberLog) {

        try {
            String routingKey = queueFactory.get(logTopic).getName();
            rabbitTemplate.convertAndSend(memberEventsTopicExchange.getName(), routingKey, memberLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

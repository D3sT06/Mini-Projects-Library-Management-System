package com.sahin.lms.library_service.model;

import com.sahin.lms.infra.enums.LogTopic;
import com.sahin.lms.library_service.config.RabbitMqConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

@Component
@ConditionalOnBean(RabbitMqConfig.class)
public class QueueFactory {

    private Map<LogTopic, Queue> topicQueueMap;

    @Autowired
    private Queue bookQueue;

    @Autowired
    private Queue itemQueue;

    @PostConstruct
    private void init() {
        topicQueueMap = new EnumMap<>(LogTopic.class);
        topicQueueMap.put(LogTopic.BOOK, bookQueue);
        topicQueueMap.put(LogTopic.BOOK_ITEM, itemQueue);
    }

    public Queue get(LogTopic topic) {
        return topicQueueMap.get(topic);
    }
}
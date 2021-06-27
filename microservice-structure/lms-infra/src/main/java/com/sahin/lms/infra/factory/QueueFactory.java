package com.sahin.lms.infra.factory;

import com.sahin.lms.infra.enums.LogTopic;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

public class QueueFactory {

    private Map<LogTopic, Queue> topicQueueMap;

    public QueueFactory(Map<LogTopic, Queue> topicQueueMap) {
        this.topicQueueMap = topicQueueMap;
    }

    public Queue get(LogTopic topic) {
        return topicQueueMap.get(topic);
    }
}
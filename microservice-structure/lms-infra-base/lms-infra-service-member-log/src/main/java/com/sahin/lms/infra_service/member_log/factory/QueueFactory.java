package com.sahin.lms.infra_service.member_log.factory;

import com.sahin.lms.infra_enum.LogTopic;
import org.springframework.amqp.core.Queue;

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
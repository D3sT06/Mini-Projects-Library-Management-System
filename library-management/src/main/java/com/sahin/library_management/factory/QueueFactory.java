package com.sahin.library_management.factory;

import com.sahin.library_management.infra.enums.LogTopic;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

@Component
public class QueueFactory {

    private Map<LogTopic, Queue> topicQueueMap;

    @Autowired
    private Queue bookQueue;

    @Autowired
    private Queue itemQueue;

    @Autowired
    private Queue loanQueue;

    @Autowired
    private Queue reservationQueue;

    @PostConstruct
    private void init() {
        topicQueueMap = new EnumMap<>(LogTopic.class);
        topicQueueMap.put(LogTopic.BOOK, bookQueue);
        topicQueueMap.put(LogTopic.BOOK_ITEM, itemQueue);
        topicQueueMap.put(LogTopic.BOOK_LOAN, loanQueue);
        topicQueueMap.put(LogTopic.BOOK_RESERVATION, reservationQueue);
    }

    public Queue get(LogTopic topic) {
        return topicQueueMap.get(topic);
    }
}
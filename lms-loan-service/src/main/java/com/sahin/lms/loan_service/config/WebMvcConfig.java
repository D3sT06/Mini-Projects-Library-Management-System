package com.sahin.lms.loan_service.config;

import com.sahin.lms.infra.enums.LogTopic;
import com.sahin.lms.infra.factory.QueueFactory;
import com.sahin.lms.infra.service.member_log.ConcreteMemberLogPublisherService;
import com.sahin.lms.infra.service.member_log.MemberLogPublisherService;
import com.sahin.lms.infra.service.member_log.VoidMemberLogPublisherService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.EnumMap;
import java.util.Map;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnBean(RabbitMqConfig.class)
    public MemberLogPublisherService concreteLogService(RabbitTemplate rabbitTemplate, TopicExchange topicExchange, Queue loanQueue, Queue reservationQueue) {

        Map<LogTopic, Queue> topicQueueMap = new EnumMap<>(LogTopic.class);
        topicQueueMap.put(LogTopic.BOOK_LOAN, loanQueue);
        topicQueueMap.put(LogTopic.BOOK_RESERVATION, reservationQueue);

        QueueFactory queueFactory = new QueueFactory(topicQueueMap);
        return new ConcreteMemberLogPublisherService(rabbitTemplate, topicExchange, queueFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public MemberLogPublisherService voidLogService() {
        return new VoidMemberLogPublisherService();
    }
}

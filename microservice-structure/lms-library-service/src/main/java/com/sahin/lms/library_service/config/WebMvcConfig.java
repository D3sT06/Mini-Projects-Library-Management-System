package com.sahin.lms.library_service.config;

import com.sahin.lms.infra_enum.LogTopic;
import com.sahin.lms.infra_service.member_log.factory.QueueFactory;
import com.sahin.lms.infra_service.member_log.service.ConcreteMemberLogPublisherService;
import com.sahin.lms.infra_service.member_log.service.MemberLogPublisherService;
import com.sahin.lms.infra_service.member_log.service.VoidMemberLogPublisherService;
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

import java.util.EnumMap;
import java.util.Map;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String[] STATIC_RESOURCE = {"/","classpath:/","classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/",
                "classpath:/resources/", "classpath:/static/", "classpath:/public/"};

        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(STATIC_RESOURCE);
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnBean(RabbitMqConfig.class)
    public MemberLogPublisherService concreteLogService(RabbitTemplate rabbitTemplate, TopicExchange topicExchange, Queue bookQueue, Queue itemQueue) {

        Map<LogTopic, Queue> topicQueueMap = new EnumMap<>(LogTopic.class);
        topicQueueMap.put(LogTopic.BOOK, bookQueue);
        topicQueueMap.put(LogTopic.BOOK_ITEM, itemQueue);

        QueueFactory queueFactory = new QueueFactory(topicQueueMap);
        return new ConcreteMemberLogPublisherService(rabbitTemplate, topicExchange, queueFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public MemberLogPublisherService voidLogService() {
        return new VoidMemberLogPublisherService();
    }
}

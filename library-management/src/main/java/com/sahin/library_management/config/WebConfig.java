package com.sahin.library_management.config;

import com.sahin.library_management.service.member_log.ConcreteMemberLogPublisherService;
import com.sahin.library_management.service.member_log.MemberLogPublisherService;
import com.sahin.library_management.service.member_log.VoidMemberLogPublisherService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    @ConditionalOnBean(RabbitMqConfig.class)
    public MemberLogPublisherService concreteLogService() {
        return new ConcreteMemberLogPublisherService();
    }

    @Bean
    @ConditionalOnMissingBean
    public MemberLogPublisherService voidLogService() {
        return new VoidMemberLogPublisherService();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

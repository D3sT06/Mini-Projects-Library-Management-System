package com.sahin.lms.loan_service.config;

import com.sahin.lms.infra.service.member_log.ConcreteMemberLogPublisherService;
import com.sahin.lms.infra.service.member_log.MemberLogPublisherService;
import com.sahin.lms.infra.service.member_log.VoidMemberLogPublisherService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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
}

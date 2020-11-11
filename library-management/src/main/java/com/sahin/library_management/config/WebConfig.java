package com.sahin.library_management.config;

import com.sahin.library_management.service.member_log.ConcreteMemberLogService;
import com.sahin.library_management.service.member_log.MemberLogService;
import com.sahin.library_management.service.member_log.VoidMemberLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    @ConditionalOnBean(RabbitMqConfig.class)
    public MemberLogService concreteLogService() {
        return new ConcreteMemberLogService();
    }

    @Bean
    @ConditionalOnMissingBean
    public MemberLogService voidLogService() {
        return new VoidMemberLogService();
    }


}

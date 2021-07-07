package com.sahin.lms.log_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"com.sahin.lms.log_service", "com.sahin.lms.infra"})
@EntityScan({"com.sahin.lms.infra.entity.log"})
@EnableJpaRepositories({"com.sahin.lms.log_service"})
public class LogServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(LogServiceApp.class, args);
    }
}
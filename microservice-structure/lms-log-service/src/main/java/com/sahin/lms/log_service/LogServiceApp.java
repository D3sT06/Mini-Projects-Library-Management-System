package com.sahin.lms.log_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"com.sahin.lms"})
@EntityScan({"com.sahin.lms"})
@EnableJpaRepositories({"com.sahin.lms"})
public class LogServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(LogServiceApp.class, args);
    }
}
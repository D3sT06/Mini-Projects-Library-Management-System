package com.sahin.lms.loan_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@ComponentScan({"com.sahin.lms"})
@EntityScan({"com.sahin.lms"})
@EnableJpaRepositories({"com.sahin.lms"})
public class LoanServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(LoanServiceApp.class, args);
    }
}

package com.sahin.lms.account_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"com.sahin.lms.account_service", "com.sahin.lms.infra"})
@EntityScan({"com.sahin.lms.infra"})
public class AccountServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApp.class, args);
    }
}

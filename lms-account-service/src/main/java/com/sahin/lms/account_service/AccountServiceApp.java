package com.sahin.lms.account_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"com.sahin.lms.account_service", "com.sahin.lms.infra"})
@EntityScan({"com.sahin.lms.infra"})
public class AccountServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApp.class, args);
    }
}

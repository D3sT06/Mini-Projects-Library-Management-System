package com.sahin.lms.apigw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Locale;

@SpringBootApplication
@ComponentScan({"com.sahin.lms.apigw", "com.sahin.lms.infra", "com.sahin.lms.infraauth"})
@EntityScan({"com.sahin.lms.infra.entity.account"})
@EnableJpaRepositories({"com.sahin.lms.apigw", "com.sahin.lms.infraauth"})
@EnableZuulProxy
@EnableDiscoveryClient
@EnableRedisRepositories({"com.sahin.lms.infraauth"})
public class ApiGatewayApp {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "US"));
        SpringApplication.run(ApiGatewayApp.class, args);
    }
}

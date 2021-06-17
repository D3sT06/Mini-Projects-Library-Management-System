package com.sahin.lms.apigw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@ComponentScan({"com.sahin.lms.apigw", "com.sahin.lms.infra"})
@EntityScan({"com.sahin.lms.infra"})
@EnableJpaRepositories({"com.sahin.lms.apigw", "com.sahin.lms.infra"})
@EnableZuulProxy
@EnableDiscoveryClient
@EnableRedisRepositories({"com.sahin.lms.infra"})
public class ApiGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApp.class, args);
    }
}

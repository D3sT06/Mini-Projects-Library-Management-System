package com.sahin.lms.infra_authorization.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("api-key.properties")
public class ApiKeyConfig {

    @Value("${api-key.header}")
    private String header;

    @Value("${api-key.value}")
    private String value;

}

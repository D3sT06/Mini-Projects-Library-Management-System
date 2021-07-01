package com.sahin.lms.infra.model.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiKeyConfig {

    @Value("${api-key.header}")
    private String header;

    @Value("${api-key.value}")
    private String value;

}

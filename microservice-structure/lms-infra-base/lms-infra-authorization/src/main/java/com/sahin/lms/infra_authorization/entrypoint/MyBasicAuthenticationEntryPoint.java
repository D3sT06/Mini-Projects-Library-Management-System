package com.sahin.lms.infra_authorization.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.lms.infra_exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse("Authentication", e.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        mapper.writeValue(response.getWriter(), errorResponse);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("dev");
        super.afterPropertiesSet();
    }
}

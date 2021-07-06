package com.sahin.lms.infra.auth;

import com.sahin.lms.infra.model.auth.ApiKeyAuthToken;
import com.sahin.lms.infra.model.auth.ApiKeyConfig;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiKeyValidationFilter extends GenericFilterBean {

    private final ApiKeyConfig apiKeyConfig;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;


    public ApiKeyValidationFilter(ApiKeyConfig apiKeyConfig, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.apiKeyConfig = apiKeyConfig;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null) {

            String apiKey = ((HttpServletRequest) servletRequest).getHeader(apiKeyConfig.getHeader());

            if (apiKey != null) {
                if (apiKey.equals(apiKeyConfig.getValue())) {
                    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
                    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_LIBRARIAN"));

                    ApiKeyAuthToken apiToken = new ApiKeyAuthToken(apiKey, grantedAuthorities);
                    SecurityContextHolder.getContext().setAuthentication(apiToken);
                } else {
                    authenticationEntryPoint.commence((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse,
                            new BadCredentialsException("Invalid API Key")
                    );
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

package com.sahin.lms.account_service.config;

import com.sahin.lms.infra_authorization.entrypoint.JwtAuthenticationEntryPoint;
import com.sahin.lms.infra_authorization.filter.ApiKeyValidationFilter;
import com.sahin.lms.infra_authorization.filter.TokenValidationFilter;
import com.sahin.lms.infra_authorization.model.ApiKeyConfig;
import com.sahin.lms.infra_authorization.service.JwtTokenDecoderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@ConditionalOnProperty(name = "security.authentication.basic", havingValue = "false")
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration",
            "/configuration/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/model/**",
            // other public endpoints of your API may be appended to this array
            "/h2-console/**"
    };

    private final JwtTokenDecoderService jwtTokenDecoderService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ApiKeyConfig apiKeyConfig;

    public JwtSecurityConfig(JwtTokenDecoderService jwtTokenDecoderService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, ApiKeyConfig apiKeyConfig) {
        this.jwtTokenDecoderService = jwtTokenDecoderService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.apiKeyConfig = apiKeyConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and().csrf().disable()

                // By default Spring Security disables rendering within an iframe because allowing a webpage to be
                // added to a frame can be a security issue, for example Clickjacking. Since H2 console runs within
                // a frame so while Spring security is enabled, frame options has to be disabled explicitly,
                // in order to get the H2 console working.
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                .addFilterBefore(new TokenValidationFilter(jwtTokenDecoderService, jwtAuthenticationEntryPoint), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new ApiKeyValidationFilter(apiKeyConfig, jwtAuthenticationEntryPoint), TokenValidationFilter.class)
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}

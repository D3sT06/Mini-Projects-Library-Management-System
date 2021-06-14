package com.sahin.lms.apigw.config;


import com.sahin.lms.infra.auth.entrypoint.MyBasicAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@ConditionalOnProperty(name = "security.authentication.basic", havingValue = "true")
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

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

    private final MyBasicAuthenticationEntryPoint authenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;

    public BasicSecurityConfig(MyBasicAuthenticationEntryPoint authenticationEntryPoint, PasswordEncoder passwordEncoder) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("librarian").password(passwordEncoder.encode("1234")).roles("LIBRARIAN")
                .and()
                .withUser("member").password(passwordEncoder.encode("1234")).roles("MEMBER");
    }
}
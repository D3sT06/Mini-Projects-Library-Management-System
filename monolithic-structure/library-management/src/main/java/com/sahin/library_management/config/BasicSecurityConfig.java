package com.sahin.library_management.config;

import com.sahin.library_management.infra.auth.MyBasicAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@ConditionalOnProperty(name = "security.authentication.basic", havingValue = "true")
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
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
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }
}

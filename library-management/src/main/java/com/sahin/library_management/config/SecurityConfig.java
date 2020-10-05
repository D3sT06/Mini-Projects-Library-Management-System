package com.sahin.library_management.config;

import com.sahin.library_management.infra.auth.*;
import com.sahin.library_management.service.LibraryCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class AuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${app.security.url.login}")
        private String loginUrl;

        private final JwtTokenGenerationService jwtTokenGenerationService;
        private final LibraryCardService libraryCardService;
        private final PasswordEncoder passwordEncoder;

        public AuthenticationSecurityConfig(JwtTokenGenerationService jwtTokenService,
                              LibraryCardService libraryCardService, PasswordEncoder passwordEncoder) {
            this.jwtTokenGenerationService = jwtTokenService;
            this.libraryCardService = libraryCardService;
            this.passwordEncoder = passwordEncoder;
        }

        @Bean
        public AbstractAuthenticationProcessingFilter authenticationFilter() throws Exception {
            return new MyAuthenticationFilter(this.loginUrl, super.authenticationManagerBean(), this.jwtTokenGenerationService);
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider(this.passwordEncoder);
            myAuthenticationProvider.setUserDetailsService(this.libraryCardService);
            return myAuthenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors()
                    .and().csrf().disable()
                    .logout().disable()
                    .formLogin().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                    .authenticationProvider(this.authenticationProvider())
                    .addFilterBefore(this.authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .antMatchers(HttpMethod.POST, loginUrl).authenticated();
        }
    }

    @Configuration
    @Order(2)
    @EnableGlobalMethodSecurity(
            prePostEnabled = true
    )
    public static class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${app.security.url.login}")
        private String loginUrl;

        @Autowired
        private JwtTokenDecoderService jwtTokenDecoderService;

        @Autowired
        private LibraryCardService libraryCardService;

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors()
                    .and().csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                    .addFilterBefore(new TokenValidationFilter(jwtTokenDecoderService, libraryCardService), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .antMatchers(HttpMethod.POST, loginUrl).denyAll()
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }
}

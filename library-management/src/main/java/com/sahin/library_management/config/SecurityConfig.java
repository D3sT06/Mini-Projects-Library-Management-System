package com.sahin.library_management.config;

import com.sahin.library_management.infra.auth.*;
import com.sahin.library_management.service.LibraryCardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class AuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${app.security.url.login}")
        private String loginUrl;

        private final JwtTokenGenerationService jwtTokenGenerationService;
        private final LibraryCardService libraryCardService;
        private final PasswordEncoder passwordEncoder;
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        public AuthenticationSecurityConfig(JwtTokenGenerationService jwtTokenService,
                                            LibraryCardService libraryCardService, PasswordEncoder passwordEncoder, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
            this.jwtTokenGenerationService = jwtTokenService;
            this.libraryCardService = libraryCardService;
            this.passwordEncoder = passwordEncoder;
            this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
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
                    .antMatcher("/api/auth/**")
                    .authorizeRequests().anyRequest().authenticated()
                    .and().cors()
                    .and().csrf().disable()
                    .logout().disable()
                    .formLogin().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                    .authenticationProvider(this.authenticationProvider())
                    .addFilterBefore(this.authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }

    @Configuration
    @Order(2)
    public static class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

        private final JwtTokenDecoderService jwtTokenDecoderService;
        private final LibraryCardService libraryCardService;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        public AuthorizationSecurityConfig(JwtTokenDecoderService jwtTokenDecoderService, LibraryCardService libraryCardService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
            this.jwtTokenDecoderService = jwtTokenDecoderService;
            this.libraryCardService = libraryCardService;
            this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
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
                    .addFilterBefore(new TokenValidationFilter(jwtTokenDecoderService, libraryCardService), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/api/librarians/getAll").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }
}

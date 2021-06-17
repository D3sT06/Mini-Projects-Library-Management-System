package com.sahin.lms.apigw.config;

import com.sahin.lms.apigw.service.AccountLoginTypeService;
import com.sahin.lms.infra.auth.*;
import com.sahin.lms.infra.service.LibraryCardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@ConditionalOnMissingBean(BasicSecurityConfig.class)
public class JwtSecurityConfig {

    @Configuration
    @Order(1)
    @ConditionalOnMissingBean(BasicSecurityConfig.class)
    public static class PasswordAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${app.security.url.password-login}")
        private String passwordLoginUrl;

        private final JwtTokenGenerationService jwtTokenGenerationService;
        private final LibraryCardService libraryCardService;
        private final PasswordEncoder passwordEncoder;
        private final UserCache userCache;
        private final AccountLoginTypeService loginTypeService;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


        public PasswordAuthenticationSecurityConfig(JwtTokenGenerationService jwtTokenService,
                                                    LibraryCardService libraryCardService, PasswordEncoder passwordEncoder,
                                                    UserCache userCache, AccountLoginTypeService loginTypeService,
                                                    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
            this.jwtTokenGenerationService = jwtTokenService;
            this.libraryCardService = libraryCardService;
            this.passwordEncoder = passwordEncoder;
            this.userCache = userCache;
            this.loginTypeService = loginTypeService;
            this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        }

        @Bean
        public UserDetailsService myUserDetailsService() {
            CachingUserDetailsService cachingUserDetailsService = new CachingUserDetailsService(libraryCardService);
            cachingUserDetailsService.setUserCache(userCache);
            return cachingUserDetailsService;
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider(this.passwordEncoder);
            myAuthenticationProvider.setUserDetailsService(myUserDetailsService());
            return myAuthenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher(this.passwordLoginUrl)
                    .authorizeRequests().anyRequest().authenticated()
                    .and().cors()
                    .and().csrf().disable()
                    .logout().disable()
                    .formLogin().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                    .authenticationProvider(this.authenticationProvider())
                    .addFilterBefore(new PasswordAuthenticationFilter(this.passwordLoginUrl, super.authenticationManagerBean(), this.jwtTokenGenerationService, loginTypeService),
                            UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }

    @Configuration
    @Order(2)
    @ConditionalOnMissingBean(BasicSecurityConfig.class)
    public static class SocialAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${app.security.url.facebook-login}")
        private String facebookLoginUrl;

        private final JwtTokenGenerationService jwtTokenGenerationService;
        private final PasswordEncoder noEncoder;
        private final AccountLoginTypeService loginTypeService;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final FacebookClient facebookClient;
        private final UserDetailsService myUserDetailsService;


        public SocialAuthenticationSecurityConfig(JwtTokenGenerationService jwtTokenService,
                                                  PasswordEncoder noEncoder, AccountLoginTypeService loginTypeService,
                                                  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                                  FacebookClient facebookClient, UserDetailsService myUserDetailsService) {
            this.jwtTokenGenerationService = jwtTokenService;
            this.noEncoder = noEncoder;
            this.loginTypeService = loginTypeService;
            this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
            this.facebookClient = facebookClient;
            this.myUserDetailsService = myUserDetailsService;
        }

        private AuthenticationProvider authenticationProvider() {
            MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider(this.noEncoder);
            myAuthenticationProvider.setUserDetailsService(myUserDetailsService);
            return myAuthenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher(this.facebookLoginUrl)
                    .authorizeRequests().anyRequest().authenticated()
                    .and().cors()
                    .and().csrf().disable()
                    .logout().disable()
                    .formLogin().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                    .authenticationProvider(this.authenticationProvider())
                    .addFilterBefore(new FacebookAuthenticationFilter(this.facebookLoginUrl, super.authenticationManagerBean(),
                                    this.jwtTokenGenerationService, loginTypeService, facebookClient),
                            UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }
}

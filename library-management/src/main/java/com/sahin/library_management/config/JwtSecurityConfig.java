package com.sahin.library_management.config;

import com.sahin.library_management.infra.auth.*;
import com.sahin.library_management.service.LibraryCardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
    public static class AuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {


        @Value("${app.security.url.login}")
        private String loginUrl;

        private final JwtTokenGenerationService jwtTokenGenerationService;
        private final LibraryCardService libraryCardService;
        private final PasswordEncoder passwordEncoder;
        private final UserCache userCache;
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


        public AuthenticationSecurityConfig(JwtTokenGenerationService jwtTokenService,
                                            LibraryCardService libraryCardService, PasswordEncoder passwordEncoder, UserCache userCache, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
            this.jwtTokenGenerationService = jwtTokenService;
            this.libraryCardService = libraryCardService;
            this.passwordEncoder = passwordEncoder;
            this.userCache = userCache;
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
                    .antMatcher("/api/auth/**")
                    .authorizeRequests().anyRequest().authenticated()
                    .and().cors()
                    .and().csrf().disable()
                    .logout().disable()
                    .formLogin().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().anonymous().and()
                    .authenticationProvider(this.authenticationProvider())
                    .addFilterBefore(new MyAuthenticationFilter(this.loginUrl, super.authenticationManagerBean(), this.jwtTokenGenerationService),
                        UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }


    @Configuration
    @Order(2)
    @ConditionalOnMissingBean(BasicSecurityConfig.class)
    public static class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

        private final JwtTokenDecoderService jwtTokenDecoderService;
        private final UserDetailsService myUserDetailsService;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        public AuthorizationSecurityConfig(JwtTokenDecoderService jwtTokenDecoderService, UserDetailsService myUserDetailsService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
            this.jwtTokenDecoderService = jwtTokenDecoderService;
            this.myUserDetailsService = myUserDetailsService;
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
                    .addFilterBefore(new TokenValidationFilter(jwtTokenDecoderService, myUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }
}

package com.sahin.library_management.config;

import com.sahin.library_management.infra.auth.*;
import com.sahin.library_management.service.AccountLoginTypeService;
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
        private final PasswordEncoder rawPassword;
        private final AccountLoginTypeService loginTypeService;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final FacebookClient facebookClient;
        private final UserDetailsService myUserDetailsService;


        public SocialAuthenticationSecurityConfig(JwtTokenGenerationService jwtTokenService,
                                                  PasswordEncoder rawPassword, AccountLoginTypeService loginTypeService,
                                                  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                                  FacebookClient facebookClient, UserDetailsService myUserDetailsService) {
            this.jwtTokenGenerationService = jwtTokenService;
            this.rawPassword = rawPassword;
            this.loginTypeService = loginTypeService;
            this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
            this.facebookClient = facebookClient;
            this.myUserDetailsService = myUserDetailsService;
        }

        private AuthenticationProvider authenticationProvider() {
            MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider(this.rawPassword);
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


    @Configuration
    @Order(3)
    @ConditionalOnMissingBean(BasicSecurityConfig.class)
    public static class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

        private static final String[] AUTH_WHITELIST = {
                // -- swagger ui
                "/swagger/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration",
                "/configuration/**",
                "/swagger-ui/**",
                "/webjars/**",
                // other public endpoints of your API may be appended to this array
                "/h2-console/**"
        };

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
                    .antMatchers(AUTH_WHITELIST).permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        }
    }
}

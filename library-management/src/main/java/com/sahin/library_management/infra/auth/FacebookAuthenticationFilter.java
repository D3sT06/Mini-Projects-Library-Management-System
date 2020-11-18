package com.sahin.library_management.infra.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.library_management.infra.model.auth.FacebookUser;
import com.sahin.library_management.infra.model.auth.LoginModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FacebookAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenGenerationService jwtTokenGenerationService;
    private final FacebookClient facebookClient;

    public FacebookAuthenticationFilter(String loginUrl, AuthenticationManager authenticationManager, JwtTokenGenerationService jwtTokenGenerationService, FacebookClient facebookClient) {
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.facebookClient = facebookClient;
        this.objectMapper = new ObjectMapper();
        this.jwtTokenGenerationService = jwtTokenGenerationService;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String fbAccessToken = objectMapper.readValue(request.getInputStream(), String.class);
        FacebookUser facebookUser = facebookClient.getUser(fbAccessToken);

        LoginModel loginModel = objectMapper.readValue(request.getInputStream(), LoginModel.class);
        return this.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(loginModel.getBarcode(), loginModel.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = this.jwtTokenGenerationService.generateToken(authResult);
        response.addHeader("Authorization", token);
    }
}

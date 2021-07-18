package com.sahin.lms.infra_authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.lms.infra_authentication.service.ILoginTypeService;
import com.sahin.lms.infra_authentication.service.JwtTokenGenerationService;
import com.sahin.lms.infra_authorization.model.LoginModel;
import com.sahin.lms.infra_enum.LoginType;
import com.sahin.lms.infra_exception.MyRuntimeException;
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

public class PasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenGenerationService jwtTokenGenerationService;
    private final ILoginTypeService loginTypeService;

    public PasswordAuthenticationFilter(String loginUrl, AuthenticationManager authenticationManager, JwtTokenGenerationService jwtTokenGenerationService, ILoginTypeService loginTypeService) {
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.loginTypeService = loginTypeService;
        this.objectMapper = new ObjectMapper();
        this.jwtTokenGenerationService = jwtTokenGenerationService;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        LoginModel loginModel = objectMapper.readValue(request.getInputStream(), LoginModel.class);

        if (!loginTypeService.doesExist(loginModel.getBarcode(), LoginType.PASSWORD)) {
            throw new MyRuntimeException("Password authentication not exists for this account");
        }

        return this.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(loginModel.getBarcode(), loginModel.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = this.jwtTokenGenerationService.getToken(authResult);
        response.addHeader("Authorization", token);
    }
}

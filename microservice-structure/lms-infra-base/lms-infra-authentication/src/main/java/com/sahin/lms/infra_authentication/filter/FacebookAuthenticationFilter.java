package com.sahin.lms.infra_authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.lms.infra_authentication.client.FacebookClient;
import com.sahin.lms.infra_authentication.service.ILoginTypeService;
import com.sahin.lms.infra_authentication.service.JwtTokenGenerationService;
import com.sahin.lms.infra_authorization.model.AccountLoginType;
import com.sahin.lms.infra_authorization.model.FacebookLoginModel;
import com.sahin.lms.infra_authorization.model.FacebookUser;
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

public class FacebookAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenGenerationService jwtTokenGenerationService;
    private final ILoginTypeService loginTypeService;
    private final FacebookClient facebookClient;

    public FacebookAuthenticationFilter(String loginUrl, AuthenticationManager authenticationManager, JwtTokenGenerationService jwtTokenGenerationService, ILoginTypeService loginTypeService, FacebookClient facebookClient) {
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.loginTypeService = loginTypeService;
        this.facebookClient = facebookClient;
        this.objectMapper = new ObjectMapper();
        this.jwtTokenGenerationService = jwtTokenGenerationService;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        FacebookLoginModel facebookLoginModel = objectMapper.readValue(request.getInputStream(), FacebookLoginModel.class);
        FacebookUser facebookUser = facebookClient.getUser(facebookLoginModel.getAccessToken());
        AccountLoginType loginType = loginTypeService.findByType(facebookUser.getId(), LoginType.FACEBOOK);

        if (loginType == null) {
            throw new MyRuntimeException("Facebook authentication not exists for this account");
        }

        String barcode = loginType.getLibraryCard().getBarcode();
        String password = loginType.getLibraryCard().getPassword();

        return this.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(barcode, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = this.jwtTokenGenerationService.getToken(authResult);
        response.addHeader("Authorization", token);
    }
}

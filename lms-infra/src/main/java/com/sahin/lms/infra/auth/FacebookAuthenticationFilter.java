package com.sahin.lms.infra.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahin.lms.infra.enums.LoginType;
import com.sahin.lms.infra.exception.MyRuntimeException;
import com.sahin.lms.infra.model.account.AccountLoginType;
import com.sahin.lms.infra.model.auth.FacebookLoginModel;
import com.sahin.lms.infra.model.auth.FacebookUser;
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
    private final AccountLoginTypeService accountLoginTypeService;
    private final FacebookClient facebookClient;

    public FacebookAuthenticationFilter(String loginUrl, AuthenticationManager authenticationManager, JwtTokenGenerationService jwtTokenGenerationService, AccountLoginTypeService accountLoginTypeService, FacebookClient facebookClient) {
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.accountLoginTypeService = accountLoginTypeService;
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
        AccountLoginType loginType = accountLoginTypeService.findByType(facebookUser.getId(), LoginType.FACEBOOK);

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

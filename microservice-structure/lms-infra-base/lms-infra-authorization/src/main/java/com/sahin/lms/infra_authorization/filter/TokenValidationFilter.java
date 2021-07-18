package com.sahin.lms.infra_authorization.filter;

import com.sahin.lms.infra_authorization.entrypoint.JwtAuthenticationEntryPoint;
import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_authorization.service.JwtTokenDecoderService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenValidationFilter extends GenericFilterBean {

    private final JwtTokenDecoderService jwtTokenDecoderService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public TokenValidationFilter(JwtTokenDecoderService jwtTokenDecoderService, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtTokenDecoderService = jwtTokenDecoderService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = this.resolveToken((HttpServletRequest) request);
        String username = null;

        if (StringUtils.hasText(token)) {
            try {
                username = jwtTokenDecoderService.getUsernameFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.createUserDetails(token);

                    if (this.jwtTokenDecoderService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                authenticationEntryPoint.commence((HttpServletRequest) request, (HttpServletResponse) response,
                        new BadCredentialsException("Unable to get JWT Token")
                );
            } catch (ExpiredJwtException e) {
                authenticationEntryPoint.commence((HttpServletRequest) request, (HttpServletResponse) response,
                        new BadCredentialsException("JWT Token has expired")
                );
            } catch (Exception e) {
                authenticationEntryPoint.commence((HttpServletRequest) request, (HttpServletResponse) response,
                        new BadCredentialsException("Invalid JWT")
                );
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String jwt = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwt = bearerToken.substring(7);
        }
        return jwt;
    }

    private UserDetails createUserDetails(String token) {
        LibraryCard libraryCard = new LibraryCard();
        libraryCard.setAccountFor(jwtTokenDecoderService.getAccountFor(token));
        libraryCard.setActive(jwtTokenDecoderService.isActive(token));
        libraryCard.setBarcode(jwtTokenDecoderService.getUsernameFromToken(token));

        return libraryCard;
    }
}

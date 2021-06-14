package com.sahin.lms.infra.auth;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public TokenValidationFilter(JwtTokenDecoderService jwtTokenDecoderService, UserDetailsService userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtTokenDecoderService = jwtTokenDecoderService;
        this.userDetailsService = userDetailsService;
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
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

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
}

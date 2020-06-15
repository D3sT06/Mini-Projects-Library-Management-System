package com.sahin.library_management.infra.auth;

import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.service.LibraryCardService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
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
import java.io.IOException;

public class TokenValidationFilter extends GenericFilterBean {

    private final JwtTokenDecoderService jwtTokenDecoderService;
    private final LibraryCardService libraryCardService;

    public TokenValidationFilter(JwtTokenDecoderService jwtTokenDecoderService, LibraryCardService libraryCardService) {
        this.jwtTokenDecoderService = jwtTokenDecoderService;
        this.libraryCardService = libraryCardService;
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
                    UserDetails userDetails = this.libraryCardService.loadUserByUsername(username);

                    if (this.jwtTokenDecoderService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new MyRuntimeException("Unable to get JWT Token", HttpStatus.BAD_REQUEST);
            } catch (ExpiredJwtException e) {
                throw new MyRuntimeException("JWT Token has expired", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                throw new MyRuntimeException("Invalid JWT", HttpStatus.BAD_REQUEST);
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

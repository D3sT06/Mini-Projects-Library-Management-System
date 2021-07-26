package com.sahin.library_management.infra.auth;

import com.sahin.library_management.infra.model.account.LibraryCard;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerationService {

    @Value("${app.security.jwt.privateKey}")
    private String privateKey;

    @Value("${app.security.jwt.ttl}")
    private long ttl;

    public String getToken(Authentication authentication) {

        String cardBarcode = ((LibraryCard) authentication.getPrincipal()).getBarcode();
        return generateToken(authentication, cardBarcode);
    }

    private String generateToken(Authentication authentication, String cardBarcode) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(System.currentTimeMillis() + this.ttl * 1000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authentication.getAuthorities());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(cardBarcode)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS512, this.privateKey).compact();

        token = String.format("Bearer %s", token);
        return token;
    }
}
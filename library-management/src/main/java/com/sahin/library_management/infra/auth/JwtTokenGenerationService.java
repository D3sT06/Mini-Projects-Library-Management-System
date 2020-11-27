package com.sahin.library_management.infra.auth;

import com.sahin.library_management.infra.entity.TokenEntity;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtTokenGenerationService {

    @Value("${app.security.jwt.privateKey}")
    private String privateKey;

    @Value("${app.security.jwt.ttl}")
    private long ttl;

    @Autowired
    private TokenRepository tokenRepository;

    public String getToken(Authentication authentication) {

        String cardBarcode = ((LibraryCard) authentication.getPrincipal()).getBarcode();
        Optional<String> tokenFromCache = getTokenFromCache(cardBarcode);
        if (tokenFromCache.isPresent())
            return tokenFromCache.get();

        String token = generateToken(authentication, cardBarcode);

        saveTokenIntoCache(cardBarcode, token);

        return token;
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

    private void saveTokenIntoCache(String cardBarcode, String token) {

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setCardBarcode(cardBarcode);
        tokenEntity.setTtl(this.ttl);

        tokenRepository.save(tokenEntity);
    }

    private Optional<String> getTokenFromCache(String cardBarcode) {
        Optional<TokenEntity> optionalToken = tokenRepository.findById(cardBarcode);
        return optionalToken.map(TokenEntity::getToken);
    }
}
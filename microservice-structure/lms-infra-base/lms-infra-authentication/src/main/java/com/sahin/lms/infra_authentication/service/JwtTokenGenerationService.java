package com.sahin.lms.infra_authentication.service;

import com.sahin.lms.infra_authentication.repository.TokenRepository;
import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_entity.account.redis.TokenEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@PropertySource("jwt.properties")
public class JwtTokenGenerationService {

    @Value("${app.security.jwt.privateKey}")
    private String privateKey;

    @Value("${app.security.jwt.ttl}")
    private long ttl;

    @Autowired
    private TokenRepository tokenRepository;

    public String getToken(Authentication authentication) {

        LibraryCard card = (LibraryCard) authentication.getPrincipal();
        Optional<String> tokenFromCache = getTokenFromCache(card.getBarcode());

        if (tokenFromCache.isPresent())
            return tokenFromCache.get();

        String token = generateToken(card);

        saveTokenIntoCache(card.getBarcode(), token);

        return token;
    }

    private String generateToken(LibraryCard libraryCard) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(System.currentTimeMillis() + this.ttl * 1000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountFor", libraryCard.getAccountFor());
        claims.put("active", libraryCard.getActive());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(libraryCard.getBarcode())
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
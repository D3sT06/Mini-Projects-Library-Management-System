package com.sahin.lms.infra.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Service
public class JwtTokenDecoderService {

    @Value("${app.security.jwt.privateKey}")
    private String privateKey;

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = this.getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        Claims claims = decodeJwt(token);
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = decodeJwt(token);
        return claims.getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims decodeJwt(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(privateKey))
                .parseClaimsJws(token).getBody();
    }
}

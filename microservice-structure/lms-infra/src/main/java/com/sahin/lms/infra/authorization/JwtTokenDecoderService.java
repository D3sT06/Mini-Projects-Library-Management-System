package com.sahin.lms.infra.authorization;

import com.sahin.lms.infra.enums.AccountFor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
@PropertySource("jwt.properties")
public class JwtTokenDecoderService {

    @Value("${app.security.jwt.privateKey}")
    private String privateKey;

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = this.getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isActive(token));
    }

    public String getUsernameFromToken(String token) {
        Claims claims = decodeJwt(token);
        return claims.getSubject();
    }

    public Boolean isActive(String token) {
        Claims claims = decodeJwt(token);
        return (Boolean) claims.get("active");
    }

    public AccountFor getAccountFor(String token) {
        Claims claims = decodeJwt(token);
        return AccountFor.valueOf((String) claims.get("accountFor"));
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

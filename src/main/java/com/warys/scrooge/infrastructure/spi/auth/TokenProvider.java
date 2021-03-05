package com.warys.scrooge.infrastructure.spi.auth;

import com.warys.scrooge.domain.model.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String SECRET = "ThisIsASecret";

    @Value("${auth.token.expiration.days}")
    private int expirationDays;

    public String generateFrom(User user) {
        return Jwts.builder()
                .setSubject("authentication token")
                .setExpiration(Date.from(LocalDateTime.now().plusYears(expirationDays).toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .claim("userId", user.getId())
                .compact();
    }

    public String getSecret() {
        return SECRET;
    }

}

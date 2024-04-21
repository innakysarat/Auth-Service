package ru.hse.actiongame.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.DECADES;

@Component
public class JwtTokenService implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;
    private static final long JWT_TOKEN_VALIDITY = DECADES.getDuration().toSeconds();

    private final SecretKey secretKey;

    public JwtTokenService(@Value(value = "${jwt.secret.key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(subject)  // user's username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000L))
                .signWith(secretKey)
                .claim("name", subject)
                .compact();

    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getClaims(@NonNull String token) {
        return getClaims(token, secretKey);
    }
}

package com.sosmoch.schadenflow.auth.internal;


import com.sosmoch.schadenflow.user.UserView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(Optional<UserView> user) {
        return user.map(u -> Jwts.builder()
                .subject(user.get().id().toString())
                .claim("email", user.get().email())
                .claim("role", user.get().role().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact())
                .orElseThrow(() -> new IllegalArgumentException("User must be present to generate token"));
    }

    Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    UUID extractUserId(String token) {
        return UUID.fromString(extractClaims(token).getSubject());
    }

    String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    boolean isTokenValid(String token) {
        try {
            return extractClaims(token).getExpiration().after(new Date());
        }
        catch (JwtException | IllegalArgumentException e) {
           return false;
        }
    }
}

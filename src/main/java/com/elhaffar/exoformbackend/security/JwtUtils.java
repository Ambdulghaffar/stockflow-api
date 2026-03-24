package com.elhaffar.exoformbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    // Injection professionnelle via le constructeur
    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access.expiration}") long accessExp,
            @Value("${jwt.refresh.expiration}") long refreshExp
    ) {
        // Transformation du String en SecretKey (HMAC-SHA)
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessExp;
        this.refreshTokenExpiration = refreshExp;
    }

    public String generateToken(String email, String role) {
        return buildToken(email, role, accessTokenExpiration);
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, null, refreshTokenExpiration);
    }

    private String buildToken(String email, String role, long exp) {
        var builder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp));
        if (role != null) builder.claim("role", role);
        return builder.signWith(key).compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpirationTime() {
        return accessTokenExpiration / 1000; // Retourne en secondes pour le DTO
    }
}

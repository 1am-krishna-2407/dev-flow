package com.devflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${devflow.jwt.secret}")
    private String secret;

    @Value("${devflow.jwt.expiration-millis}")
    private long expirationMillis;

    @Value("${devflow.jwt.refresh-expiration-millis}")
    private long refreshExpirationMillis;

    private Key signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpirationMillis);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean isTokenValid(String token, String userEmail) {
        try {
            String subject = parseToken(token).getSubject();
            return subject.equals(userEmail) && !isTokenExpired(token);
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public String extractSubject(String token) {
        return parseToken(token).getSubject();
    }
}

package com.devflow.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setField(jwtService, "secret", "testSecretKeyAtLeast32BytesLong!!");
        setField(jwtService, "expirationMillis", 900000L);
        setField(jwtService, "refreshExpirationMillis", 2592000000L);
        jwtService.init();
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtService.generateToken("user@test.com", new HashMap<>());
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateRefreshToken_shouldReturnNonNullToken() {
        String token = jwtService.generateRefreshToken("user@test.com", new HashMap<>());
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void parseToken_shouldExtractCorrectSubject() {
        String email = "user@test.com";
        String token = jwtService.generateToken(email, new HashMap<>());
        String subject = jwtService.parseToken(token).getSubject();
        assertEquals(email, subject);
    }

    @Test
    void parseToken_shouldPreserveCustomClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = jwtService.generateToken("user@test.com", claims);
        String role = jwtService.parseToken(token).get("role", String.class);
        assertEquals("ADMIN", role);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String email = "user@test.com";
        String token = jwtService.generateToken(email, new HashMap<>());
        assertTrue(jwtService.isTokenValid(token, email));
    }

    @Test
    void isTokenValid_shouldReturnFalseForWrongEmail() {
        String token = jwtService.generateToken("user@test.com", new HashMap<>());
        assertFalse(jwtService.isTokenValid(token, "other@test.com"));
    }

    @Test
    void isTokenValid_shouldReturnFalseForMalformedToken() {
        assertFalse(jwtService.isTokenValid("not.a.valid.jwt", "user@test.com"));
    }

    @Test
    void isTokenExpired_shouldReturnFalseForFreshToken() {
        String token = jwtService.generateToken("user@test.com", new HashMap<>());
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void isTokenExpired_shouldThrowForExpiredToken() throws Exception {
        JwtService shortLivedService = new JwtService();
        setField(shortLivedService, "secret", "testSecretKeyAtLeast32BytesLong!!");
        setField(shortLivedService, "expirationMillis", 1L); // 1ms expiration
        setField(shortLivedService, "refreshExpirationMillis", 1L);
        shortLivedService.init();

        String token = shortLivedService.generateToken("user@test.com", new HashMap<>());
        Thread.sleep(50); // wait for expiration
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> shortLivedService.isTokenExpired(token));
    }

    @Test
    void extractSubject_shouldReturnEmail() {
        String email = "user@test.com";
        String token = jwtService.generateToken(email, new HashMap<>());
        assertEquals(email, jwtService.extractSubject(token));
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

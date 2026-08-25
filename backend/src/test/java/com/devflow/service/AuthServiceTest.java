package com.devflow.service;

import com.devflow.dto.AuthResponse;
import com.devflow.dto.LoginRequest;
import com.devflow.dto.RegistrationRequest;
import com.devflow.entity.Role;
import com.devflow.entity.User;
import com.devflow.repository.UserRepository;
import com.devflow.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@devflow.local");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.DEVELOPER);
    }

    @Test
    void register_shouldReturnBothTokens() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Test User");
        request.setEmail("test@devflow.local");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test@devflow.local")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(eq("test@devflow.local"), anyMap())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(eq("test@devflow.local"), anyMap())).thenReturn("refresh-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenEmailExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("existing@devflow.local");

        when(userRepository.existsByEmail("existing@devflow.local")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnBothTokens() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@devflow.local");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@devflow.local")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(eq("test@devflow.local"), anyMap())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(eq("test@devflow.local"), anyMap())).thenReturn("refresh-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowOnBadCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@devflow.local");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void refreshToken_shouldReturnNewTokenPair() {
        when(jwtService.extractSubject("old-refresh-token")).thenReturn("test@devflow.local");
        when(jwtService.isTokenExpired("old-refresh-token")).thenReturn(false);
        when(userRepository.findByEmail("test@devflow.local")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(eq("test@devflow.local"), anyMap())).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(eq("test@devflow.local"), anyMap())).thenReturn("new-refresh-token");

        AuthResponse response = authService.refreshToken("old-refresh-token");

        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
    }

    @Test
    void refreshToken_shouldThrowOnExpiredToken() {
        when(jwtService.extractSubject("expired-token")).thenReturn("test@devflow.local");
        when(jwtService.isTokenExpired("expired-token")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.refreshToken("expired-token"));
    }
}

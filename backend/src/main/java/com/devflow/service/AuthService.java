package com.devflow.service;

import com.devflow.dto.AuthResponse;
import com.devflow.dto.LoginRequest;
import com.devflow.dto.RegistrationRequest;
import com.devflow.entity.Role;
import com.devflow.entity.User;
import com.devflow.repository.UserRepository;
import com.devflow.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DEVELOPER);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user.getEmail(), new HashMap<>());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), new HashMap<>());
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid login credentials"));

        String accessToken = jwtService.generateToken(user.getEmail(), new HashMap<>());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), new HashMap<>());
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse refreshToken(String token) {
        String email = jwtService.extractSubject(token);
        if (email == null || jwtService.isTokenExpired(token)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newAccessToken = jwtService.generateToken(user.getEmail(), new HashMap<>());
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail(), new HashMap<>());
        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer");
    }
}

package com.devflow.service;

import com.devflow.dto.UserRequest;
import com.devflow.entity.User;
import com.devflow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(Long id, UserRequest request) {
        return userRepository.findById(id)
            .map(user -> {
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setDepartment(request.getDepartment());
                user.setDesignation(request.getDesignation());
                user.setSkills(request.getSkills());
                user.setProfilePictureUrl(request.getProfilePictureUrl());
                user.setRole(request.getRole() != null ? request.getRole() : user.getRole());
                user.setActive(request.isActive());
                user.setEmailVerified(request.isEmailVerified());
                return userRepository.save(user);
            })
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }
}

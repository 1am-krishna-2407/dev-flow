package com.devflow.dto;

import com.devflow.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String department;
    private String designation;
    private String skills;
    private String profilePictureUrl;
    private boolean active;
    private boolean emailVerified;
}

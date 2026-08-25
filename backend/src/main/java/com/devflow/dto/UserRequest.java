package com.devflow.dto;

import com.devflow.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String department;
    private String designation;
    private String skills;
    private String profilePictureUrl;
    private Role role;
    private boolean active = true;
    private boolean emailVerified = false;
}

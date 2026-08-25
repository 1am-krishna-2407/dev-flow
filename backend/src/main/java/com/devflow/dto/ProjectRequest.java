package com.devflow.dto;

import com.devflow.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectRequest {
    @NotBlank
    private String name;

    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}

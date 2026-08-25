package com.devflow.dto;

import com.devflow.entity.Priority;
import com.devflow.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {
    @NotBlank
    private String title;

    private String description;
    private Long assigneeId;
    private Long reporterId;
    private Priority priority;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long projectId;
}

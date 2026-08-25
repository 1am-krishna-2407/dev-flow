package com.devflow.controller;

import com.devflow.dto.TaskRequest;
import com.devflow.entity.Task;
import com.devflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('DEVELOPER')")
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('DEVELOPER')")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('DEVELOPER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

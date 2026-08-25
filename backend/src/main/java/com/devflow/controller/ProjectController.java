package com.devflow.controller;

import com.devflow.dto.ProjectRequest;
import com.devflow.entity.Project;
import com.devflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

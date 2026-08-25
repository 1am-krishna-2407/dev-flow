package com.devflow.service;

import com.devflow.dto.ProjectRequest;
import com.devflow.entity.Project;
import com.devflow.entity.ProjectStatus;
import com.devflow.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");
        testProject.setDescription("Description");
        testProject.setStatus(ProjectStatus.ACTIVE);
        testProject.setStartDate(LocalDate.now());
        testProject.setEndDate(LocalDate.now().plusDays(30));
    }

    @Test
    void findAll_paginated_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> page = new PageImpl<>(List.of(testProject), pageable, 1);
        when(projectRepository.findAll(pageable)).thenReturn(page);

        Page<Project> result = projectService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Project", result.getContent().get(0).getName());
    }

    @Test
    void create_shouldSaveWithDefaultStatus() {
        ProjectRequest request = new ProjectRequest();
        request.setName("New Project");
        request.setDescription("New Desc");

        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> {
            Project p = inv.getArgument(0);
            p.setId(2L);
            return p;
        });

        Project result = projectService.create(request);

        assertNotNull(result);
        assertEquals("New Project", result.getName());
        assertEquals(ProjectStatus.PLANNING, result.getStatus());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void update_shouldModifyExistingProject() {
        ProjectRequest request = new ProjectRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Desc");
        request.setStatus(ProjectStatus.COMPLETED);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        Project result = projectService.update(1L, request);

        assertEquals("Updated Name", result.getName());
        assertEquals(ProjectStatus.COMPLETED, result.getStatus());
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        ProjectRequest request = new ProjectRequest();
        request.setName("Name");

        assertThrows(IllegalArgumentException.class, () -> projectService.update(999L, request));
    }

    @Test
    void delete_shouldCallDeleteById() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.delete(1L);

        verify(projectRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(projectRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> projectService.delete(999L));
        verify(projectRepository, never()).deleteById(any());
    }
}

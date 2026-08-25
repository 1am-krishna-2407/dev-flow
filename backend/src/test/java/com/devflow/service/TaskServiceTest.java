package com.devflow.service;

import com.devflow.dto.TaskRequest;
import com.devflow.entity.*;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.TaskRepository;
import com.devflow.repository.UserRepository;
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
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private User testUser;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Dev User");
        testUser.setEmail("dev@devflow.local");

        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Task description");
        testTask.setPriority(Priority.MEDIUM);
        testTask.setStatus(TaskStatus.TODO);
        testTask.setAssignee(testUser);
        testTask.setProject(testProject);
    }

    @Test
    void findAll_paginated_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(testTask), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(page);

        Page<Task> result = taskService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).getTitle());
    }

    @Test
    void create_shouldSaveTaskWithAssigneeAndProject() {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("Desc");
        request.setPriority(Priority.HIGH);
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setAssigneeId(1L);
        request.setProjectId(1L);
        request.setDueDate(LocalDate.now().plusDays(5));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setId(2L);
            return t;
        });

        Task result = taskService.create(request);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals(testUser, result.getAssignee());
        assertEquals(testProject, result.getProject());
    }

    @Test
    void create_shouldThrowWhenAssigneeNotFound() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Task");
        request.setAssigneeId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.create(request));
    }

    @Test
    void update_shouldModifyExistingTask() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Task");
        request.setDescription("Updated");
        request.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.update(1L, request);

        assertEquals("Updated Task", result.getTitle());
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        TaskRequest request = new TaskRequest();
        request.setTitle("Title");

        assertThrows(IllegalArgumentException.class, () -> taskService.update(999L, request));
    }

    @Test
    void delete_shouldCallDeleteById() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.delete(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> taskService.delete(999L));
        verify(taskRepository, never()).deleteById(any());
    }
}

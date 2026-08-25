package com.devflow.service;

import com.devflow.entity.TaskStatus;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.TaskRepository;
import com.devflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getTeamAnalytics_shouldReturnCorrectCounts() {
        when(projectRepository.count()).thenReturn(5L);
        when(taskRepository.count()).thenReturn(20L);
        when(userRepository.count()).thenReturn(3L);
        when(taskRepository.countByStatus(TaskStatus.DONE)).thenReturn(8L);

        Map<String, Object> analytics = analyticsService.getTeamAnalytics();

        assertEquals(5L, analytics.get("projectCount"));
        assertEquals(20L, analytics.get("taskCount"));
        assertEquals(3L, analytics.get("userCount"));
        assertEquals(8L, analytics.get("completedTasks"));
    }

    @Test
    void getTeamAnalytics_shouldReturnZerosWhenEmpty() {
        when(projectRepository.count()).thenReturn(0L);
        when(taskRepository.count()).thenReturn(0L);
        when(userRepository.count()).thenReturn(0L);
        when(taskRepository.countByStatus(TaskStatus.DONE)).thenReturn(0L);

        Map<String, Object> analytics = analyticsService.getTeamAnalytics();

        assertEquals(0L, analytics.get("projectCount"));
        assertEquals(0L, analytics.get("taskCount"));
        assertEquals(0L, analytics.get("userCount"));
        assertEquals(0L, analytics.get("completedTasks"));
    }

    @Test
    void getTeamAnalytics_shouldCallCountByStatusNotFindAll() {
        when(projectRepository.count()).thenReturn(0L);
        when(taskRepository.count()).thenReturn(0L);
        when(userRepository.count()).thenReturn(0L);
        when(taskRepository.countByStatus(TaskStatus.DONE)).thenReturn(0L);

        analyticsService.getTeamAnalytics();

        verify(taskRepository).countByStatus(TaskStatus.DONE);
        verify(taskRepository, never()).findAll();
    }
}

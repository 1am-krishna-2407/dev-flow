package com.devflow.service;

import com.devflow.entity.TaskStatus;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.TaskRepository;
import com.devflow.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public AnalyticsService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "analytics", key = "'team'")
    public Map<String, Object> getTeamAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("projectCount", projectRepository.count());
        analytics.put("taskCount", taskRepository.count());
        analytics.put("userCount", userRepository.count());
        analytics.put("completedTasks", taskRepository.countByStatus(TaskStatus.DONE));
        return analytics;
    }
}

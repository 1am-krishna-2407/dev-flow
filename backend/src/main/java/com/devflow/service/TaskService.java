package com.devflow.service;

import com.devflow.dto.TaskRequest;
import com.devflow.entity.Project;
import com.devflow.entity.Task;
import com.devflow.entity.User;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.TaskRepository;
import com.devflow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() != null ? request.getPriority() : task.getPriority());
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
            task.setAssignee(assignee);
        }
        if (request.getReporterId() != null) {
            User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));
            task.setReporter(reporter);
        }
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            task.setProject(project);
        }
        return taskRepository.save(task);
    }

    public Task update(Long id, TaskRequest request) {
        return taskRepository.findById(id)
            .map(task -> {
                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                task.setPriority(request.getPriority() != null ? request.getPriority() : task.getPriority());
                task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
                task.setDueDate(request.getDueDate());
                if (request.getAssigneeId() != null) {
                    User assignee = userRepository.findById(request.getAssigneeId())
                        .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
                    task.setAssignee(assignee);
                }
                if (request.getReporterId() != null) {
                    User reporter = userRepository.findById(request.getReporterId())
                        .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));
                    task.setReporter(reporter);
                }
                if (request.getProjectId() != null) {
                    Project project = projectRepository.findById(request.getProjectId())
                        .orElseThrow(() -> new IllegalArgumentException("Project not found"));
                    task.setProject(project);
                }
                return taskRepository.save(task);
            })
            .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}

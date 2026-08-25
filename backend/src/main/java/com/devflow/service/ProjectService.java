package com.devflow.service;

import com.devflow.dto.ProjectRequest;
import com.devflow.entity.Project;
import com.devflow.entity.ProjectStatus;
import com.devflow.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Project create(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() != null ? request.getStatus() : ProjectStatus.PLANNING);
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        return projectRepository.save(project);
    }

    public Project update(Long id, ProjectRequest request) {
        return projectRepository.findById(id)
            .map(project -> {
                project.setName(request.getName());
                project.setDescription(request.getDescription());
                project.setStatus(request.getStatus() != null ? request.getStatus() : project.getStatus());
                project.setStartDate(request.getStartDate());
                project.setEndDate(request.getEndDate());
                return projectRepository.save(project);
            })
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}

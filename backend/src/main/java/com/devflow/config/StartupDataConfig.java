package com.devflow.config;

import com.devflow.entity.Project;
import com.devflow.entity.ProjectStatus;
import com.devflow.entity.Role;
import com.devflow.entity.Task;
import com.devflow.entity.TaskStatus;
import com.devflow.entity.User;
import com.devflow.repository.ProjectRepository;
import com.devflow.repository.TaskRepository;
import com.devflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StartupDataConfig {
    @Bean
    public CommandLineRunner seedData(UserRepository userRepository,
                                      ProjectRepository projectRepository,
                                      TaskRepository taskRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User developer = new User();
                developer.setName("Alex Morgan");
                developer.setEmail("developer@devflow.local");
                developer.setPassword(passwordEncoder.encode("devflow123"));
                developer.setRole(Role.DEVELOPER);
                developer.setDepartment("Engineering");
                developer.setDesignation("Software Engineer");
                developer.setSkills("Java, React, DevOps");
                developer.setProfilePictureUrl("https://images.unsplash.com/photo-1502685104226-ee32379fefbe?auto=format&fit=crop&w=200&q=80");
                developer.setEmailVerified(true);

                User manager = new User();
                manager.setName("Priya Patel");
                manager.setEmail("manager@devflow.local");
                manager.setPassword(passwordEncoder.encode("devflow123"));
                manager.setRole(Role.PROJECT_MANAGER);
                manager.setDepartment("Product");
                manager.setDesignation("Project Manager");
                manager.setSkills("Agile, Planning, Delivery");
                manager.setProfilePictureUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=200&q=80");
                manager.setEmailVerified(true);

                userRepository.saveAll(List.of(developer, manager));

                Project project = new Project();
                project.setName("Workflow Central");
                project.setDescription("Make team collaboration easier by tracking tasks, projects, and updates in one place.");
                project.setStatus(ProjectStatus.ACTIVE);
                project.setStartDate(LocalDate.now().minusDays(25));
                project.setEndDate(LocalDate.now().plusDays(35));
                projectRepository.save(project);

                Task taskOne = new Task();
                taskOne.setTitle("Launch onboarding flow");
                taskOne.setDescription("Create a simple setup flow for new users and teams.");
                taskOne.setPriority(null);
                taskOne.setStatus(TaskStatus.IN_PROGRESS);
                taskOne.setAssignee(developer);
                taskOne.setReporter(manager);
                taskOne.setProject(project);
                taskOne.setDueDate(LocalDate.now().plusDays(5));

                Task taskTwo = new Task();
                taskTwo.setTitle("Improve analytics dashboard");
                taskTwo.setDescription("Add easy-to-read charts and daily summaries for non-technical users.");
                taskTwo.setPriority(null);
                taskTwo.setStatus(TaskStatus.TODO);
                taskTwo.setAssignee(developer);
                taskTwo.setReporter(manager);
                taskTwo.setProject(project);
                taskTwo.setDueDate(LocalDate.now().plusDays(12));

                taskRepository.saveAll(List.of(taskOne, taskTwo));
            }
        };
    }
}

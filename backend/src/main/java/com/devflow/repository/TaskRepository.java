package com.devflow.repository;

import com.devflow.entity.Task;
import com.devflow.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByStatus(TaskStatus status);
}

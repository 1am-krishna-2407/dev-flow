package com.devflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1200)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNING;

    private LocalDate startDate;
    private LocalDate endDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "assignedProjects")
    private Set<User> members = new HashSet<>();
}

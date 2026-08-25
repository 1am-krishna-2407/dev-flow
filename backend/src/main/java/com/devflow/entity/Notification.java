package com.devflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(length = 1000)
    private String message;

    private boolean readFlag = false;
    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User recipient;
}

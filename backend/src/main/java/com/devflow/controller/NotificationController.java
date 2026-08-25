package com.devflow.controller;

import com.devflow.entity.Notification;
import com.devflow.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @PostMapping("/send/{recipientId}")
    public ResponseEntity<Notification> sendNotification(@PathVariable Long recipientId, @Valid @RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.sendNotification(recipientId, notification));
    }
}

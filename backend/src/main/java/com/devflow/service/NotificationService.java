package com.devflow.service;

import com.devflow.entity.Notification;
import com.devflow.entity.User;
import com.devflow.repository.NotificationRepository;
import com.devflow.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Notification> redisTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               RedisTemplate<String, Notification> redisTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public Notification sendNotification(Long recipientId, Notification notification) {
        User user = userRepository.findById(recipientId)
            .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));
        notification.setRecipient(user);
        Notification saved = notificationRepository.save(notification);
        redisTemplate.opsForValue().set("notification:" + saved.getId(), saved, Duration.ofMinutes(30));
        return saved;
    }

    public List<Notification> getNotifications(Long recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
    }
}

package com.example.travelalert.common.controller;

import com.example.travelalert.common.model.NotificationType;
import com.example.travelalert.common.model.repository.NotificationResponse;
import com.example.travelalert.common.model.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }
    @GetMapping("/{userId}/notifications")
    public List<NotificationResponse> getForUser(
            @PathVariable String userId,
            @RequestParam(required = false) NotificationType type) {
        var notifications = (type == null)
                ? service.getForUser(userId)
                : service.getForUserByType(userId, type);
        return notifications.stream().map(NotificationResponse::from).toList();
    }
}


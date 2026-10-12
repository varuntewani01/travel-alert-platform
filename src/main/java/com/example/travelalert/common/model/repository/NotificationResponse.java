package com.example.travelalert.common.model.repository;

import com.example.travelalert.common.model.Notification;

import java.time.Instant;

public record NotificationResponse(String notificationId,
                                   String userId,
                                   String type,
                                   String referenceId,
                                   String message,
                                   Instant createdAt) {
public static NotificationResponse from(Notification n){
    return new NotificationResponse(  n.getId(), n.getUserId(), n.getType().name(),
            n.getRefereneId(), n.getMessage(), n.getCreatedAt());
}

}

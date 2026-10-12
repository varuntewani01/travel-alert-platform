package com.example.travelalert.common.model.service;

import com.example.travelalert.common.model.Notification;
import com.example.travelalert.common.model.NotificationType;
import com.example.travelalert.common.model.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository reposiotry){
        this.repository = reposiotry;
    }

    @Transactional
    public Notification create(String userId, NotificationType type, String referenceId, String message){
        Notification n = new Notification();
        n.setId(UUID.randomUUID().toString());
        n.setUserId(userId);
        n.setType(type);
        n.setRefereneId(referenceId);
        n.setMessage(message);
        n.setCreatedAt(Instant.now());
        return repository.save(n);
    }

    @Transactional(readOnly = true)
    public List<Notification> getForUser(String userId){
        return repository.findUserByIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getForUserByType(String userId, NotificationType type){
        return repository.findByUserIdAndTypeOrderByCreatedAtDesc(userId,type);
    }

}

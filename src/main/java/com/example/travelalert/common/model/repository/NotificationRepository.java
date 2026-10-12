package com.example.travelalert.common.model.repository;

import com.example.travelalert.common.model.Notification;
import com.example.travelalert.common.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;


public interface NotificationRepository extends JpaRepository<Notification,String> {
List<Notification> findUserByIdOrderByCreatedAtDesc(String userId);
List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(String userId, NotificationType type);

}

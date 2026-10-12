package com.example.travelalert.common.model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Entity
@Table(name = "notification", indexes = {@Index(name = "idx_notif_user_created", columnList = "userId,createdAt")})
public class Notification {

@Id
private String id;

@Column(nullable = false)
private String userId;

@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 20)
private NotificationType type;

@Column(nullable = false)
private String refereneId;

@Column(nullable = false, length = 500)
private String message;

@Column(nullable = false)
private Instant createdAt;

public Notification(){

}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getRefereneId() {
        return refereneId;
    }

    public void setRefereneId(String refereneId) {
        this.refereneId = refereneId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

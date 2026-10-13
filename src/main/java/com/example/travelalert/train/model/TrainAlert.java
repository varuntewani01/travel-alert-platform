package com.example.travelalert.train.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "train_alerts")
public class TrainAlert {
    @Id
    private String id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, length = 10)
    private String pnr;
    @ElementCollection
    @CollectionTable(name = "train_alert_transitions", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "transition_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<NotifyTransition> notifyOn = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainAlertStatus status;
    @Column(nullable = false)
    private Instant createdAt;
    private Instant triggeredAt;

    public TrainAlert() { }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }
    public Set<NotifyTransition> getNotifyOn() { return notifyOn; }
    public void setNotifyOn(Set<NotifyTransition> notifyOn) { this.notifyOn = notifyOn; }
    public TrainAlertStatus getStatus() { return status; }
    public void setStatus(TrainAlertStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(Instant triggeredAt) { this.triggeredAt = triggeredAt; }
}

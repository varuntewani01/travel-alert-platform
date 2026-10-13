package com.example.travelalert.train.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "pnr_statuses")
public class PnrStatus {
    @Id
    @Column(length = 10)
    private String pnr;
    @Column(nullable = false)
    private String currentStatus;
    private String previousStatus;
    private String trainNumber;
    private String trainName;
    private LocalDate travelDate;
    private Instant updatedAt;
    public PnrStatus() { }
    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

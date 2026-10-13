package com.example.travelalert.flight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "flight_alerts", indexes ={@Index(name= "idx_flight_alert_lookup", columnList = "origin,destination,travelDate,status")})
public class FlightAlert {

    @Id
    private String id;

    private String userId;

    private String origin;

    private String destination;

    private LocalDate travelDate;

    private BigDecimal maxPrice;

    private AlertStatus status;

    private Instant triggeredAt;

    private Instant createdAt;

    public FlightAlert() {
    }

    public boolean matches(String origin, String destination, LocalDate travelDate, BigDecimal price){
        return this.origin.equalsIgnoreCase(origin) && this.destination.equalsIgnoreCase(destination)
                && this.travelDate.equals(travelDate) && price.compareTo(this.maxPrice) <=0;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public Instant getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Instant triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

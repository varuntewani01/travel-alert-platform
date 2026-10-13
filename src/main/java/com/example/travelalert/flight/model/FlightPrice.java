package com.example.travelalert.flight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "flight_prices")
public class FlightPrice {
    @Id
    private String id;
    private BigDecimal price;
    private Instant updatedAt;

    public FlightPrice() { }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

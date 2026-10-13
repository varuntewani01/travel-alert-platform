package com.example.travelalert.flight.dto;

import com.example.travelalert.flight.model.AlertStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record FlightAlertResponse(String id, String userId, String origin, String destination,
                                  LocalDate travelDate, BigDecimal maxPrice, AlertStatus status,
                                  Instant createdAt, Instant triggeredAt) { }

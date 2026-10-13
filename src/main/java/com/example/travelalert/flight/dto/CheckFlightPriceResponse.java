package com.example.travelalert.flight.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record CheckFlightPriceResponse(String origin, String destination, LocalDate travelDate,
                                       BigDecimal currentPrice, Instant updatedAt,
                                       List<TriggeredAlertResponse> triggeredAlerts) { }

package com.example.travelalert.flight.dto;

import java.math.BigDecimal;

public record TriggeredAlertResponse(String alertId, String userId, BigDecimal targetPrice) { }

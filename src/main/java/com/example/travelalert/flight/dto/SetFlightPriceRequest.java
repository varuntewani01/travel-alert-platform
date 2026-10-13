package com.example.travelalert.flight.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SetFlightPriceRequest(@NotBlank String origin, @NotBlank String destination,
                                    @NotNull @FutureOrPresent LocalDate travelDate,
                                    @NotNull @Positive BigDecimal price) { }

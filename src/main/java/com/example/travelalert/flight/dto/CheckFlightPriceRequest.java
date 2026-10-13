package com.example.travelalert.flight.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CheckFlightPriceRequest(@NotBlank String origin, @NotBlank String destination,
                                      @NotNull @FutureOrPresent LocalDate travelDate) { }

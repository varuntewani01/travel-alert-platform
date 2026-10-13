package com.example.travelalert.train.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpsertPnrStatusRequest(
        @NotBlank @Pattern(regexp = "\\d{10}") String pnr,
        @NotBlank String currentStatus,
        String previousStatus,
        String trainNumber,
        String trainName,
        @FutureOrPresent LocalDate travelDate) { }

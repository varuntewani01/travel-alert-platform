package com.example.travelalert.train.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CheckPnrRequest(@NotBlank @Pattern(regexp = "\\d{10}") String pnr) { }

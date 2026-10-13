package com.example.travelalert.train.dto;

import com.example.travelalert.train.model.NotifyTransition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record CreateTrainAlertRequest(
        @NotBlank String userId,
        @NotBlank @Pattern(regexp = "\\d{10}") String pnr,
        @NotEmpty Set<NotifyTransition> notifyOn) { }

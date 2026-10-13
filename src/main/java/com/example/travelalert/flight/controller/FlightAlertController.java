package com.example.travelalert.flight.controller;

import com.example.travelalert.flight.dto.CreateFlightAlertRequest;
import com.example.travelalert.flight.dto.FlightAlertResponse;
import com.example.travelalert.flight.service.FlightAlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightAlertController {
    private final FlightAlertService service;
    public FlightAlertController(FlightAlertService service) { this.service = service; }

    @PostMapping("/api/flights/alerts")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightAlertResponse create(@Valid @RequestBody CreateFlightAlertRequest request) { return service.create(request); }

    @GetMapping("/api/flights/alerts/{id}")
    public FlightAlertResponse get(@PathVariable String id) { return service.get(id); }
}

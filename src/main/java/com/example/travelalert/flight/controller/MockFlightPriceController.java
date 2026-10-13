package com.example.travelalert.flight.controller;

import com.example.travelalert.flight.dto.SetFlightPriceRequest;
import com.example.travelalert.flight.model.FlightPrice;
import com.example.travelalert.flight.service.FlightPriceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockFlightPriceController {
    private final FlightPriceService service;
    public MockFlightPriceController(FlightPriceService service) { this.service = service; }

    @PostMapping("/api/mock/flights/prices")
    public FlightPrice setPrice(@Valid @RequestBody SetFlightPriceRequest request) { return service.setPrice(request); }
}

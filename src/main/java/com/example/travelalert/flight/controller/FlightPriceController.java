package com.example.travelalert.flight.controller;

import com.example.travelalert.flight.dto.CheckFlightPriceRequest;
import com.example.travelalert.flight.dto.CheckFlightPriceResponse;
import com.example.travelalert.flight.service.FlightPriceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightPriceController {
    private final FlightPriceService service;
    public FlightPriceController(FlightPriceService service) { this.service = service; }

    @PostMapping("/api/flights/check-price")
    public CheckFlightPriceResponse check(@Valid @RequestBody CheckFlightPriceRequest request) {
        return service.checkPrice(request);
    }
}

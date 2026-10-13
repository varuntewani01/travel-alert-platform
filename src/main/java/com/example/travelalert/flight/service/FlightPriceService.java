package com.example.travelalert.flight.service;

import com.example.travelalert.common.exception.ResourceNotFoundException;
import com.example.travelalert.flight.dto.CheckFlightPriceRequest;
import com.example.travelalert.flight.dto.CheckFlightPriceResponse;
import com.example.travelalert.flight.dto.SetFlightPriceRequest;
import com.example.travelalert.flight.dto.TriggeredAlertResponse;
import com.example.travelalert.flight.model.FlightAlert;
import com.example.travelalert.flight.model.FlightPrice;
import com.example.travelalert.flight.repository.FlightPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class FlightPriceService {
    private final FlightPriceRepository priceRepository;
    private final FlightAlertService alertService;

    public FlightPriceService(FlightPriceRepository priceRepository, FlightAlertService alertService) {
        this.priceRepository = priceRepository;
        this.alertService = alertService;
    }

    @Transactional
    public FlightPrice setPrice(SetFlightPriceRequest request) {
        String origin = request.origin().trim().toUpperCase();
        String destination = request.destination().trim().toUpperCase();
        String id = priceId(origin, destination, request.travelDate());
        FlightPrice price = priceRepository.findById(id).orElseGet(FlightPrice::new);
        price.setId(id);
        price.setPrice(request.price());
        price.setUpdatedAt(Instant.now());
        return priceRepository.save(price);
    }

    @Transactional
    public CheckFlightPriceResponse checkPrice(CheckFlightPriceRequest request) {
        String origin = request.origin().trim().toUpperCase();
        String destination = request.destination().trim().toUpperCase();
        FlightPrice price = priceRepository.findById(priceId(origin, destination, request.travelDate()))
                .orElseThrow(() -> new ResourceNotFoundException("Flight price not found for " + origin + " → "
                        + destination + " on " + request.travelDate()));
        ArrayList<TriggeredAlertResponse> triggered = new ArrayList<>();
        for (FlightAlert alert : alertService.findActive(origin, destination, request.travelDate())) {
            if (alert.matches(origin, destination, request.travelDate(), price.getPrice())
                    && alertService.markTriggered(alert, price.getPrice())) {
                triggered.add(new TriggeredAlertResponse(alert.getId(), alert.getUserId(), alert.getMaxPrice()));
            }
        }
        return new CheckFlightPriceResponse(origin, destination, request.travelDate(), price.getPrice(),
                price.getUpdatedAt(), triggered);
    }

    private String priceId(String origin, String destination, java.time.LocalDate date) {
        return origin + "-" + destination + "-" + DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }
}

package com.example.travelalert.flight.service;

import com.example.travelalert.common.exception.ResourceNotFoundException;
import com.example.travelalert.common.model.NotificationType;
import com.example.travelalert.common.service.NotificationService;
import com.example.travelalert.flight.dto.CreateFlightAlertRequest;
import com.example.travelalert.flight.dto.FlightAlertResponse;
import com.example.travelalert.flight.model.AlertStatus;
import com.example.travelalert.flight.model.FlightAlert;
import com.example.travelalert.flight.repository.FlightAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FlightAlertService {
    private final FlightAlertRepository repository;
    private final NotificationService notificationService;

    public FlightAlertService(FlightAlertRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    @Transactional
    public FlightAlertResponse create(CreateFlightAlertRequest request) {
        FlightAlert alert = new FlightAlert();
        alert.setId(UUID.randomUUID().toString());
        alert.setUserId(request.userId());
        alert.setOrigin(request.origin().trim().toUpperCase());
        alert.setDestination(request.destination().trim().toUpperCase());
        alert.setTravelDate(request.travelDate());
        alert.setMaxPrice(request.maxPrice());
        alert.setStatus(AlertStatus.ACTIVE);
        alert.setCreatedAt(Instant.now());
        return response(repository.save(alert));
    }

    @Transactional(readOnly = true)
    public FlightAlertResponse get(String id) {
        return response(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight alert not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<FlightAlert> findActive(String origin, String destination, java.time.LocalDate travelDate) {
        return repository.findByOriginAndDestinationAndTravelDateAndStatus(
                origin.trim().toUpperCase(), destination.trim().toUpperCase(), travelDate, AlertStatus.ACTIVE);
    }

    @Transactional
    public boolean markTriggered(FlightAlert alert, BigDecimal currentPrice) {
        Instant now = Instant.now();
        if (repository.transitionStatus(alert.getId(), AlertStatus.ACTIVE, AlertStatus.TRIGGERED, now) != 1) {
            return false;
        }
        String message = alert.getOrigin() + " → " + alert.getDestination() + " on "
                + DateTimeFormatter.ISO_LOCAL_DATE.format(alert.getTravelDate()) + " dropped to ₹"
                + currentPrice.stripTrailingZeros().toPlainString() + " (your target ₹"
                + alert.getMaxPrice().stripTrailingZeros().toPlainString() + ")";
        notificationService.create(alert.getUserId(), NotificationType.FLIGHT, alert.getId(), message);
        return true;
    }

    private FlightAlertResponse response(FlightAlert alert) {
        return new FlightAlertResponse(alert.getId(), alert.getUserId(), alert.getOrigin(), alert.getDestination(),
                alert.getTravelDate(), alert.getMaxPrice(), alert.getStatus(), alert.getCreatedAt(), alert.getTriggeredAt());
    }
}

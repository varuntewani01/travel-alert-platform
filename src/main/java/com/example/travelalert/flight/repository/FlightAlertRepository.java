package com.example.travelalert.flight.repository;

import com.example.travelalert.flight.model.FlightAlert;
import com.example.travelalert.flight.model.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface FlightAlertRepository extends JpaRepository<FlightAlert, String> {
    List<FlightAlert> findByOriginAndDestinationAndTravelDateAndStatus(
            String origin, String destination, LocalDate travelDate, AlertStatus status);

    @Modifying
    @Query("update FlightAlert a set a.status = :newStatus, a.triggeredAt = :triggeredAt where a.id = :id and a.status = :currentStatus")
    int transitionStatus(@Param("id") String id, @Param("currentStatus") AlertStatus currentStatus,
                         @Param("newStatus") AlertStatus newStatus, @Param("triggeredAt") Instant triggeredAt);
}

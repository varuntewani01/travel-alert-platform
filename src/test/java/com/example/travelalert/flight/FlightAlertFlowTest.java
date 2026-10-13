package com.example.travelalert.flight;

import com.example.travelalert.common.repository.NotificationRepository;
import com.example.travelalert.flight.dto.CheckFlightPriceRequest;
import com.example.travelalert.flight.dto.CreateFlightAlertRequest;
import com.example.travelalert.flight.dto.SetFlightPriceRequest;
import com.example.travelalert.flight.dto.FlightAlertResponse;
import com.example.travelalert.flight.model.AlertStatus;
import com.example.travelalert.flight.service.FlightAlertService;
import com.example.travelalert.flight.service.FlightPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FlightAlertFlowTest {
    private final FlightAlertService alertService;
    private final FlightPriceService priceService;
    private final NotificationRepository notificationRepository;

    FlightAlertFlowTest(FlightAlertService alertService, FlightPriceService priceService,
                        NotificationRepository notificationRepository) {
        this.alertService = alertService;
        this.priceService = priceService;
        this.notificationRepository = notificationRepository;
    }

    @Test
    void priceAtOrBelowMaxTriggersAlertAndCreatesNotification() {
        LocalDate travelDate = LocalDate.now().plusDays(1);
        FlightAlertResponse alert = alertService.create(new CreateFlightAlertRequest("flight-flow-user", "DEL",
                "DXB", travelDate, new BigDecimal("25000")));
        priceService.setPrice(new SetFlightPriceRequest("DEL", "DXB", travelDate, new BigDecimal("23500")));

        priceService.checkPrice(new CheckFlightPriceRequest("DEL", "DXB", travelDate));

        assertEquals(AlertStatus.TRIGGERED, alertService.get(alert.id()).status());
        assertEquals(1, notificationRepository.findAll().stream()
                .filter(notification -> alert.id().equals(notification.getRefereneId())).count());
        assertEquals("DEL → DXB on " + travelDate + " dropped to ₹23500 (your target ₹25000)",
                notificationRepository.findAll().stream()
                        .filter(notification -> alert.id().equals(notification.getRefereneId()))
                        .findFirst().orElseThrow().getMessage());
    }
}

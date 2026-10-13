package com.example.travelalert.flight;

import com.example.travelalert.common.repository.NotificationRepository;
import com.example.travelalert.flight.dto.CheckFlightPriceRequest;
import com.example.travelalert.flight.dto.CreateFlightAlertRequest;
import com.example.travelalert.flight.dto.FlightAlertResponse;
import com.example.travelalert.flight.dto.SetFlightPriceRequest;
import com.example.travelalert.flight.service.FlightAlertService;
import com.example.travelalert.flight.service.FlightPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FlightConcurrentTriggerTest {
    private final FlightAlertService alertService;
    private final FlightPriceService priceService;
    private final NotificationRepository notificationRepository;

    FlightConcurrentTriggerTest(FlightAlertService alertService, FlightPriceService priceService,
                                NotificationRepository notificationRepository) {
        this.alertService = alertService;
        this.priceService = priceService;
        this.notificationRepository = notificationRepository;
    }

    @Test
    void simultaneousPriceChecksCreateExactlyOneNotification() throws Exception {
        LocalDate travelDate = LocalDate.now().plusDays(1);
        FlightAlertResponse alert = alertService.create(new CreateFlightAlertRequest("flight-concurrent-user", "BOM",
                "LHR", travelDate, new BigDecimal("50000")));
        priceService.setPrice(new SetFlightPriceRequest("BOM", "LHR", travelDate, new BigDecimal("45000")));
        ExecutorService executor = Executors.newFixedThreadPool(20);
        try {
            @SuppressWarnings("unchecked")
            Future<?>[] checks = new Future<?>[20];
            for (int i = 0; i < checks.length; i++) {
                checks[i] = executor.submit(() -> priceService.checkPrice(
                        new CheckFlightPriceRequest("BOM", "LHR", travelDate)));
            }
            for (Future<?> check : checks) check.get();
        } finally {
            executor.shutdownNow();
        }
        assertEquals(1, notificationRepository.findAll().stream()
                .filter(notification -> alert.id().equals(notification.getRefereneId())).count());
    }
}

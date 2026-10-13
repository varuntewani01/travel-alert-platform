package com.example.travelalert.train;

import com.example.travelalert.common.repository.NotificationRepository;
import com.example.travelalert.train.dto.CreateTrainAlertRequest;
import com.example.travelalert.train.dto.UpsertPnrStatusRequest;
import com.example.travelalert.train.model.NotifyTransition;
import com.example.travelalert.train.model.TrainAlert;
import com.example.travelalert.train.service.TrainAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TrainConcurrentTriggerTest {
    private final TrainAlertService service;
    private final NotificationRepository notificationRepository;

    TrainConcurrentTriggerTest(TrainAlertService service, NotificationRepository notificationRepository) {
        this.service = service;
        this.notificationRepository = notificationRepository;
    }

    @Test
    void simultaneousPnrChecksCreateExactlyOneNotification() throws Exception {
        String pnr = "9876543210";
        TrainAlert alert = service.create(new CreateTrainAlertRequest("concurrent-user", pnr,
                Set.of(NotifyTransition.WL_TO_RAC)));
        service.upsert(new UpsertPnrStatusRequest(pnr, "RAC 12", "WL 93", "12001", "Example Express",
                LocalDate.now()));
        ExecutorService executor = Executors.newFixedThreadPool(20);
        try {
            @SuppressWarnings("unchecked")
            Future<?>[] checks = new Future<?>[20];
            for (int i = 0; i < checks.length; i++) checks[i] = executor.submit(() -> service.check(pnr));
            for (Future<?> check : checks) check.get();
        } finally {
            executor.shutdownNow();
        }
        assertEquals(1, notificationRepository.findAll().stream()
                .filter(notification -> alert.getId().equals(notification.getRefereneId())).count());
    }
}

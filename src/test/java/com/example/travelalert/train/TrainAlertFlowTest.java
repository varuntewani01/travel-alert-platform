package com.example.travelalert.train;

import com.example.travelalert.common.repository.NotificationRepository;
import com.example.travelalert.train.dto.CheckPnrRequest;
import com.example.travelalert.train.dto.CreateTrainAlertRequest;
import com.example.travelalert.train.dto.UpsertPnrStatusRequest;
import com.example.travelalert.train.model.NotifyTransition;
import com.example.travelalert.train.model.TrainAlert;
import com.example.travelalert.train.model.TrainAlertStatus;
import com.example.travelalert.train.service.TrainAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TrainAlertFlowTest {
    private final TrainAlertService service;
    private final NotificationRepository notificationRepository;

    TrainAlertFlowTest(TrainAlertService service, NotificationRepository notificationRepository) {
        this.service = service;
        this.notificationRepository = notificationRepository;
    }

    @Test
    void pnrTransitionTriggersAlertAndCreatesNotification() {
        String pnr = "1234567890";
        TrainAlert alert = service.create(new CreateTrainAlertRequest("flow-user", pnr,
                Set.of(NotifyTransition.WL_TO_RAC)));
        service.upsert(new UpsertPnrStatusRequest(pnr, "RAC 12", "WL 93", "12001", "Example Express",
                LocalDate.now()));

        service.check(pnr);

        assertEquals(TrainAlertStatus.TRIGGERED, service.get(alert.getId()).getStatus());
        assertEquals(1, notificationRepository.findAll().stream()
                .filter(notification -> alert.getId().equals(notification.getRefereneId())).count());
    }
}

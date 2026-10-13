package com.example.travelalert.train.service;

import com.example.travelalert.common.exception.ResourceNotFoundException;
import com.example.travelalert.common.model.NotificationType;
import com.example.travelalert.common.service.NotificationService;
import com.example.travelalert.train.dto.CreateTrainAlertRequest;
import com.example.travelalert.train.dto.UpsertPnrStatusRequest;
import com.example.travelalert.train.model.NotifyTransition;
import com.example.travelalert.train.model.PnrStatus;
import com.example.travelalert.train.model.TrainAlert;
import com.example.travelalert.train.model.TrainAlertStatus;
import com.example.travelalert.train.repository.PnrStatusRepository;
import com.example.travelalert.train.repository.TrainAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TrainAlertService {
    private final TrainAlertRepository alertRepository;
    private final PnrStatusRepository pnrRepository;
    private final NotificationService notificationService;

    public TrainAlertService(TrainAlertRepository alertRepository, PnrStatusRepository pnrRepository,
                             NotificationService notificationService) {
        this.alertRepository = alertRepository;
        this.pnrRepository = pnrRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public TrainAlert create(CreateTrainAlertRequest request) {
        TrainAlert alert = new TrainAlert();
        alert.setId(UUID.randomUUID().toString());
        alert.setUserId(request.userId());
        alert.setPnr(request.pnr());
        alert.setNotifyOn(request.notifyOn());
        alert.setStatus(TrainAlertStatus.ACTIVE);
        alert.setCreatedAt(Instant.now());
        return alertRepository.save(alert);
    }

    @Transactional(readOnly = true)
    public TrainAlert get(String id) {
        return alertRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train alert not found: " + id));
    }

    @Transactional
    public PnrStatus upsert(UpsertPnrStatusRequest request) {
        PnrStatus status = pnrRepository.findById(request.pnr()).orElseGet(PnrStatus::new);
        status.setPnr(request.pnr());
        status.setCurrentStatus(request.currentStatus());
        status.setPreviousStatus(request.previousStatus());
        status.setTrainNumber(request.trainNumber());
        status.setTrainName(request.trainName());
        status.setTravelDate(request.travelDate());
        status.setUpdatedAt(Instant.now());
        return pnrRepository.save(status);
    }

    @Transactional
    public PnrStatus check(String pnr) {
        PnrStatus status = pnrRepository.findById(pnr)
                .orElseThrow(() -> new ResourceNotFoundException("PNR status not found: " + pnr));
        for (TrainAlert alert : alertRepository.findByPnrAndStatus(pnr, TrainAlertStatus.ACTIVE)) {
            NotifyTransition transition = matchingTransition(status);
            if (transition != null && alert.getNotifyOn().contains(transition)) {
                Instant now = Instant.now();
                int updated = alertRepository.transitionStatus(alert.getId(), TrainAlertStatus.ACTIVE,
                        TrainAlertStatus.TRIGGERED, now);
                if (updated == 1) {
                    notificationService.create(alert.getUserId(), NotificationType.TRAVEL, alert.getId(),
                            "Train PNR " + pnr + " status changed to " + status.getCurrentStatus());
                }
            }
        }
        return status;
    }

    private NotifyTransition matchingTransition(PnrStatus status) {
        String current = normalize(status.getCurrentStatus());
        String previous = normalize(status.getPreviousStatus());
        if (current.contains("CHART") && current.contains("PREPARED")) return NotifyTransition.CHART_PREPARED;
        if (current.equals("CNF") || current.startsWith("CNF ")) {
            if (previous.startsWith("RAC")) return NotifyTransition.RAC_TO_CNF;
            return NotifyTransition.CNF;
        }
        if (current.startsWith("RAC") && previous.startsWith("WL")) return NotifyTransition.WL_TO_RAC;
        return null;
    }

    private String normalize(String value) { return value == null ? "" : value.trim().toUpperCase(); }
}

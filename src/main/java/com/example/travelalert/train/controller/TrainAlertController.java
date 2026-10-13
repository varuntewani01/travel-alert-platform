package com.example.travelalert.train.controller;

import com.example.travelalert.train.dto.CheckPnrRequest;
import com.example.travelalert.train.dto.CreateTrainAlertRequest;
import com.example.travelalert.train.dto.UpsertPnrStatusRequest;
import com.example.travelalert.train.model.PnrStatus;
import com.example.travelalert.train.model.TrainAlert;
import com.example.travelalert.train.service.TrainAlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainAlertController {
    private final TrainAlertService service;
    public TrainAlertController(TrainAlertService service) { this.service = service; }

    @PostMapping("/api/trains/alerts")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainAlert create(@Valid @RequestBody CreateTrainAlertRequest request) { return service.create(request); }

    @GetMapping("/api/trains/alerts/{alertId}")
    public TrainAlert get(@PathVariable String alertId) { return service.get(alertId); }

    @PostMapping("/api/trains/check-pnr")
    public PnrStatus check(@Valid @RequestBody CheckPnrRequest request) { return service.check(request.pnr()); }

    @PostMapping("/api/mock/trains/pnr")
    public PnrStatus upsert(@Valid @RequestBody UpsertPnrStatusRequest request) { return service.upsert(request); }
}

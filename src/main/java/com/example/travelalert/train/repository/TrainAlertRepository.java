package com.example.travelalert.train.repository;

import com.example.travelalert.train.model.TrainAlert;
import com.example.travelalert.train.model.TrainAlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TrainAlertRepository extends JpaRepository<TrainAlert, String> {
    List<TrainAlert> findByPnrAndStatus(String pnr, TrainAlertStatus status);

    @Modifying
    @Query("update TrainAlert a set a.status = :newStatus, a.triggeredAt = :triggeredAt where a.id = :id and a.status = :currentStatus")
    int transitionStatus(@Param("id") String id, @Param("currentStatus") TrainAlertStatus currentStatus,
                         @Param("newStatus") TrainAlertStatus newStatus, @Param("triggeredAt") Instant triggeredAt);
}

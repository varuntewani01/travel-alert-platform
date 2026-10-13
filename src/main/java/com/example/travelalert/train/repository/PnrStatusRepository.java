package com.example.travelalert.train.repository;

import com.example.travelalert.train.model.PnrStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PnrStatusRepository extends JpaRepository<PnrStatus, String> { }

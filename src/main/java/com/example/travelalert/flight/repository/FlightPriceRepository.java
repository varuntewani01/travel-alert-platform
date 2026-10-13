package com.example.travelalert.flight.repository;

import com.example.travelalert.flight.model.FlightPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightPriceRepository extends JpaRepository<FlightPrice, String> { }

package com.automated.parkinglot.repository.application;

import com.automated.parkinglot.models.application.parking.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {}

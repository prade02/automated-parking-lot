package com.automated.parkinglot.repository;

import org.springframework.stereotype.Repository;
import com.automated.parkinglot.models.parking.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {
}

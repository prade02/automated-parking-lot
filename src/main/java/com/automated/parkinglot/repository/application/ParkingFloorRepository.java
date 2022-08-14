package com.automated.parkinglot.repository.application;

import com.automated.parkinglot.models.application.parking.ParkingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ParkingFloorRepository extends JpaRepository<ParkingFloor, Integer> {
  Iterable<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId);
}

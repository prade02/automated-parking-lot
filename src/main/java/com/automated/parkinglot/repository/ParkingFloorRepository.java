package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.parking.ParkingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ParkingFloorRepository extends JpaRepository<ParkingFloor, Integer> {
    Iterable<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId);
}

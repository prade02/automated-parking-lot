package com.automated.parkinglot.repository;

import org.springframework.stereotype.Repository;
import com.automated.parkinglot.models.parking.ParkingLot;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ParkingLotRepository extends CrudRepository<ParkingLot, Integer> {
}

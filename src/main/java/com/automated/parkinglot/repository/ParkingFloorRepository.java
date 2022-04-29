package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.parking.ParkingFloor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ParkingFloorRepository extends CrudRepository<ParkingFloor, Integer> {
    @Query(value = "select * from parking_floor where parking_lot= :parkingLotId", nativeQuery = true)
    Iterable<ParkingFloor> getAllParkingFloorsByParkingLot(@Param("parkingLotId") int parkingLotId);
}

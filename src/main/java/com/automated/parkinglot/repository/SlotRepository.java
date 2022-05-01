package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.parking.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    @Query(value = "select * from slot where parking_floor = :parkingFloorId", nativeQuery = true)
    Iterable<Slot> findAllSlotsByParkingFloor(@Param("parkingFloorId") int parkingFloorId);

    @Query(value = "select * from slot where name = :name", nativeQuery = true)
    Optional<Slot> findByName(@Param("name") String name);

    @Procedure("select_available_slot")
    Integer getAvailableSlot(int parkingLotId, String slotType);
}

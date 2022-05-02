package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    @Query(value = "SELECT * FROM slot WHERE parking_floor = :parkingFloorId", nativeQuery = true)
    Iterable<Slot> findAllSlotsByParkingFloor(@Param("parkingFloorId") int parkingFloorId);

    @Query(value = "SELECT * FROM slot WHERE name = :name", nativeQuery = true)
    Optional<Slot> findByName(@Param("name") String name);

    @Procedure("select_available_slot")
    Integer getAvailableSlot(int parkingLotId, String slotType);

    @Query(value = "SELECT s.* FROM slot s JOIN parking_floor f ON s.parking_floor = f.id WHERE f.parking_lot =" +
            " :parkingLot AND slot_status = :status ORDER BY s.name", nativeQuery = true)
    Iterable<Slot> getAllSlotsForStatus(@Param("status") String status, @Param("parkingLot") int parkingLotId);
}

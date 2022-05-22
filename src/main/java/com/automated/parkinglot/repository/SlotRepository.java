package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.parking.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Iterable<Slot> findAllSlotsByParkingFloor(int parkingFloorId);
    Optional<Slot> findByName(String name);
    Integer getAvailableSlot(int parkingLotId, String slotType);
    Iterable<Slot> getAllSlotsForStatus(String status, int parkingLotId);
}

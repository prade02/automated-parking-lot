package com.automated.parkinglot.repository.application;

import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SlotRepository extends JpaRepository<Slot, Integer> {
  Iterable<Slot> findAllSlotsByParkingFloor(int parkingFloorId);

  Optional<Slot> findByName(String name);

  Iterable<Slot> getAllSlotsForStatus(SlotStatus status, int parkingLotId);
}

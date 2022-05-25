package com.automated.parkinglot.strategies.slot_booking;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.repository.SlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LowestOnAllFromDb implements SlotBookingStrategy {

  private final SlotRepository slotRepository;

  @Override
  public Slot bookSlot(int parkingLotId, GenericType slotType) {
    var slot = slotRepository.getAvailableSlot(parkingLotId, slotType);
    if (slot == null) throw new InvalidRequestException("No slots available");
    return slot;
  }
}

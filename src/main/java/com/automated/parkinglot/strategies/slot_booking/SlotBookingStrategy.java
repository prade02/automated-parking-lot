package com.automated.parkinglot.strategies.slot_booking;

import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.parking.Slot;

public interface SlotBookingStrategy {
  Slot bookSlot(int parkingLotId, GenericType slotType);
}

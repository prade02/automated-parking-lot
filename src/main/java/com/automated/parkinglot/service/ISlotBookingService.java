package com.automated.parkinglot.service;

import com.automated.parkinglot.models.enums.GenericType;

public interface ISlotBookingService {
    String bookSlot(int parkingLotId, String vehicleRegistration, GenericType slotType);
}

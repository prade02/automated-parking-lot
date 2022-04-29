package com.automated.parkinglot.service;

import com.automated.parkinglot.models.parking.Slot;

import java.util.List;

public interface ISlotService {

    List<Slot> findAllSlotsForFloor(int parkingFloorId);
    Slot getSlotById(int slotId);
    Slot getSlotByName(String name);
    Slot addNewSlot(Slot slot);
    Slot updateSlot(int slotId, Slot slot);
    void deleteSlotById(int id);
    void deleteSlotByName(String name);
}

package com.automated.parkinglot.service;

import com.automated.parkinglot.models.parking.Slot;

import java.util.List;
import java.util.Map;

public interface ISlotService {

    List<Slot> getAllSlotsForFloor(int parkingFloorId);

    Slot getSlotById(int slotId);

    Slot getSlotByName(String name);

    Slot addNewSlot(Slot slot);

    Slot updateSlot(Slot slot);

    void deleteSlotById(int id);

    void deleteSlotByName(String name);

    Map<Integer, Map<String, Integer>> getCountOfVacantSlotsPerFloorPerType(int parkingLotId);

    Iterable<Slot> getAllVacantSlotsPerFloorPerType(int parkingLotId);

    Iterable<Slot> getAllOccupiedSlotsPerFloorPerType(int parkingLotId);
}

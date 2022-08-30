package com.automated.parkinglot.service;

import com.automated.parkinglot.dto.PagedContents;
import com.automated.parkinglot.models.application.parking.Slot;

import java.util.Map;

public interface ISlotService {

    PagedContents<Slot> getAllSlotsForFloor(int parkingFloorId, int pageNumber, boolean setPageInfo);

    Slot getSlotById(int slotId);

    Slot getSlotByName(String name);

    Slot addNewSlot(Slot slot);

    Slot updateSlot(Slot slot);

    void deleteSlotById(int id);

    void deleteSlotByName(String name);

    Map<Integer, Map<String, Integer>> getCountOfVacantSlotsPerFloorPerType(int parkingLotId);

    Iterable<Slot> getAllVacantSlotsPerFloorPerType(int parkingLotId);

    Iterable<Slot> getAllOccupiedSlotsPerFloorPerType(int parkingLotId);

    Iterable<Slot> addNewSlots(Iterable<Slot> slots);

    void deleteSlotsById(Iterable<Integer> ids);
}

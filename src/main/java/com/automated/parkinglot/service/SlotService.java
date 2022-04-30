package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.repository.ParkingFloorRepository;
import com.automated.parkinglot.repository.SlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SlotService implements ISlotService {

    private final SlotRepository slotRepository;
    private final ParkingFloorRepository parkingFloorRepository;

    @Override
    public List<Slot> getAllSlotsForFloor(int parkingFloorId) {
        var slots = new ArrayList<Slot>();
        slotRepository.findAllSlotsByParkingFloor(parkingFloorId).iterator().forEachRemaining(slots::add);
        return slots;
    }

    @Override
    public Slot getSlotById(int slotId) {
        var optionalSlot = slotRepository.findById(slotId);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("No slot found for given Id");
        return optionalSlot.get();
    }

    @Override
    public Slot getSlotByName(String name) {
        var optionalSlot = slotRepository.findByName(name);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("No slot found for given name");
        return optionalSlot.get();
    }

    @Override
    public Slot addNewSlot(Slot slot) {
        var optionalParkingFloor = parkingFloorRepository.findById(slot.getParkingFloor());
        if (optionalParkingFloor.isEmpty())
            throw new InvalidRequestException("Invalid parking floor");
        var parkingFloor = optionalParkingFloor.get();
        if (!canAddNewSlots(parkingFloor))
            throw new InvalidRequestException("Can not add new slots to this floor");
        slot.setName(String.format("%s_%s", parkingFloor.getName(), slot.getName()));
        return slotRepository.save(slot);
    }

    @Override
    public Slot updateSlot(int slotId, Slot slot) {
        if (slotId != slot.getSlotId())
            throw new InvalidRequestException("Id passed and Id in entity does not match");
        else if (slotRepository.findById(slotId).isEmpty())
            throw new InvalidRequestException("Id not found");

        return slotRepository.save(slot);
    }

    @Override
    public void deleteSlotById(int id) {
        final var optionalSlot = slotRepository.findById(id);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("Id not found");

        slotRepository.delete(optionalSlot.get());
    }

    @Override
    public void deleteSlotByName(String name) {
        final var optionalSlot = slotRepository.findByName(name);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("Name not found");

        slotRepository.delete(optionalSlot.get());
    }

    private boolean canAddNewSlots(ParkingFloor parkingFloor) {
        return parkingFloor.getTotalSlots() > getAllSlotsForFloor(parkingFloor.getParkingFloorId()).size();
    }
}

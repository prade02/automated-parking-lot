package com.automated.parkinglot.service;

import com.automated.parkinglot.dto.PagedContents;
import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.repository.application.SlotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SlotService implements ISlotService {

    private final SlotRepository slotRepository;
    private final int pageSize;

    public SlotService(SlotRepository slotRepository, @Value("${pagination.slot.size}") int pageSize) {
        this.slotRepository = slotRepository;
        this.pageSize = pageSize;
    }

    @Override
    public PagedContents<Slot> getAllSlotsForFloor(int parkingFloorId, int pageNumber, boolean setPageInfo) {
        pageNumber = pageNumber == 0 ? 0 : pageNumber - 1;
        return slotRepository.findAllSlotsByParkingFloor(parkingFloorId, pageNumber, pageSize, setPageInfo);
    }

    @Override
    public Slot getSlotById(int slotId) {
        var optionalSlot = slotRepository.findById(slotId);
        if (optionalSlot.isEmpty()) throw new InvalidRequestException("No slot found for given Id");
        return optionalSlot.get();
    }

    @Override
    public Slot getSlotByName(String name) {
        var optionalSlot = slotRepository.findByName(name);
        if (optionalSlot.isEmpty()) throw new InvalidRequestException("No slot found for given name");
        return optionalSlot.get();
    }

    @Override
    public Slot addNewSlot(Slot slot) {
        slot.setName(String.format("%s_%s", slot.getParkingFloor().getName(), slot.getName()));
        return slotRepository.save(slot);
    }

    @Override
    public Slot updateSlot(Slot slot) {
        if (slotRepository.findById(slot.getSlotId()).isEmpty())
            throw new InvalidRequestException("Id not found");

        return slotRepository.save(slot);
    }

    @Override
    public void deleteSlotById(int id) {
        final var optionalSlot = slotRepository.findById(id);
        if (optionalSlot.isEmpty()) throw new InvalidRequestException("Id not found");

        slotRepository.delete(optionalSlot.get());
    }

    @Override
    public void deleteSlotByName(String name) {
        final var optionalSlot = slotRepository.findByName(name);
        if (optionalSlot.isEmpty()) throw new InvalidRequestException("Name not found");

        slotRepository.delete(optionalSlot.get());
    }

//    @Deprecated
//    private boolean canAddNewSlots(ParkingFloor parkingFloor) {
//        return parkingFloor.getTotalSlots()
//                > getAllSlotsForFloor(parkingFloor.getParkingFloorId()).size();
//    }

    @Override
    public Map<Integer, Map<String, Integer>> getCountOfVacantSlotsPerFloorPerType(int parkingLotId) {
        return getCountOfSlotsPerFloorPerType(parkingLotId, SlotStatus.VACANT);
    }

    @Override
    public Iterable<Slot> getAllVacantSlotsPerFloorPerType(int parkingLotId) {
        return getAllSlotsPerFloorForType(parkingLotId, SlotStatus.VACANT);
    }

    @Override
    public Iterable<Slot> getAllOccupiedSlotsPerFloorPerType(int parkingLotId) {
        return getAllSlotsPerFloorForType(parkingLotId, SlotStatus.OCCUPIED);
    }

    private Map<Integer, Map<String, Integer>> getCountOfSlotsPerFloorPerType(
            int parkingLotId, SlotStatus slotStatus) {
        final var vacantSlots = slotRepository.getAllSlotsForStatus(slotStatus, parkingLotId);
        final var vacantSlotsPerFloorForType = new TreeMap<Integer, Map<String, Integer>>();
        for (Slot vacantSlot : vacantSlots) {
            if (!vacantSlotsPerFloorForType.containsKey(vacantSlot.getParkingFloor().getParkingFloorId()))
                vacantSlotsPerFloorForType.put(
                        vacantSlot.getParkingFloor().getParkingFloorId(), new HashMap<>());
            var floor = vacantSlotsPerFloorForType.get(vacantSlot.getParkingFloor().getParkingFloorId());
            String sSlotType = vacantSlot.getSlotType().name();
            if (!floor.containsKey(sSlotType)) floor.put(sSlotType, 0);
            floor.put(sSlotType, floor.get(sSlotType) + 1);
        }
        return vacantSlotsPerFloorForType;
    }

    private Iterable<Slot> getAllSlotsPerFloorForType(int parkingLotId, SlotStatus slotStatus) {
        return slotRepository.getAllSlotsForStatus(slotStatus, parkingLotId);
    }

    @Override
    public Iterable<Slot> addNewSlots(Iterable<Slot> slots) {
        slots.forEach(slot -> slot.setName(String.format("%s_%s", slot.getParkingFloor().getName(), slot.getName())));
        return slotRepository.saveAll(slots);
    }

    @Override
    public void deleteSlotsById(Iterable<Integer> ids) {
        slotRepository.deleteAllByIdInBatch(ids);
    }
}

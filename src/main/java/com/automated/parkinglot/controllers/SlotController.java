package com.automated.parkinglot.controllers;

import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.service.ISlotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/slot")
public class SlotController {

    private final ISlotService slotService;

    @GetMapping("all/{parkingFloorId}")
    public List<Slot> getAllSlots(@PathVariable int parkingFloorId) {
        return slotService.getAllSlotsForFloor(parkingFloorId);
    }

    @GetMapping("{id}")
    public Slot getParkingSlot(@PathVariable int id) {
        return slotService.getSlotById(id);
    }

    @PostMapping
    public Slot saveNewSlot(@RequestBody Slot slot) {
        return slotService.addNewSlot(slot);
    }

    @PutMapping("{id}")
    public Slot updateSlot(@PathVariable int id, @RequestBody Slot slot) {
        return slotService.updateSlot(id, slot);
    }

    @DeleteMapping("{id}")
    public void deleteSlot(@PathVariable int id) {
        slotService.deleteSlotById(id);
    }

    @GetMapping("vacant/count/{parkingLotId}")
    public Map<Integer, Map<String, Integer>> getCountOfVacantSlotsPerFloorForType(@PathVariable int parkingLotId) {
        return slotService.getCountOfVacantSlotsPerFloorPerType(parkingLotId);
    }

    @GetMapping("vacant/all/{parkingLotId}")
    public Map<Integer, Map<String, List<Slot>>> getAllVacantSlotsPerFloorForType(@PathVariable int parkingLotId) {
        return slotService.getAllVacantSlotsPerFloorPerType(parkingLotId);
    }

    @GetMapping("occupied/all/{parkingLotId}")
    public Map<Integer, Map<String, List<Slot>>> getAllOccupiedSlotsPerFloorForType(@PathVariable int parkingLotId) {
        return slotService.getAllOccupiedSlotsPerFloorPerType(parkingLotId);
    }
}

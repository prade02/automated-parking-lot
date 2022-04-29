package com.automated.parkinglot.controllers;

import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.service.ISlotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/slot")
public class SlotController {

    private final ISlotService slotService;

    @GetMapping("all/{parkingFloorId}")
    public List<Slot> getAllSlots(@PathVariable int parkingFloorId) {
        return slotService.findAllSlotsForFloor(parkingFloorId);
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
}

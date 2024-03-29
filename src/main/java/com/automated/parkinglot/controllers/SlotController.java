package com.automated.parkinglot.controllers;

import com.automated.parkinglot.dto.PagedContents;
import com.automated.parkinglot.dto.SlotDTO;
import com.automated.parkinglot.models.application.parking.ParkingFloor;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.service.IParkingFloorService;
import com.automated.parkinglot.service.ISlotService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/slot")
public class SlotController {

    private final ISlotService slotService;
    private final IParkingFloorService parkingFloorService;
    private final ModelMapper modelMapper;

    // TODO: batch get operation for all api's

    @GetMapping("all/{parkingFloorId}")
    public PagedContents<Slot> getAllSlots(@PathVariable int parkingFloorId,
                                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNumber,
                                           @RequestParam(value = "needPageInfo", required = false, defaultValue = "true")
                                                   boolean setPageInfo) {
        return slotService.getAllSlotsForFloor(parkingFloorId, pageNumber, setPageInfo);
    }

    @GetMapping("{id}")
    public SlotDTO getParkingSlot(@PathVariable int id) {
        return modelMapper.map(slotService.getSlotById(id), SlotDTO.class);
    }

    @PostMapping("{parkingFloorId}")
    public SlotDTO saveNewSlot(@PathVariable int parkingFloorId, @RequestBody SlotDTO slotDTO) {
        ParkingFloor parkingFloor = parkingFloorService.getParkingFloor(parkingFloorId);
        Slot newSlot =
                slotService.addNewSlot(
                        Slot.builder()
                                .parkingFloor(parkingFloor)
                                .name(slotDTO.getName())
                                .slotStatus(slotDTO.getSlotStatus())
                                .slotType(slotDTO.getSlotType())
                                .build());
        return modelMapper.map(newSlot, SlotDTO.class);
    }

    @PutMapping("{id}")
    public SlotDTO updateSlot(@RequestBody SlotDTO slotDTO) {
        return modelMapper.map(
                slotService.updateSlot(modelMapper.map(slotDTO, Slot.class)), SlotDTO.class);
    }

    @DeleteMapping("{id}")
    public void deleteSlot(@PathVariable int id) {
        slotService.deleteSlotById(id);
    }

    @GetMapping("vacant/count/{parkingLotId}")
    public Map<Integer, Map<String, Integer>> getCountOfVacantSlotsPerFloorForType(
            @PathVariable int parkingLotId) {
        return slotService.getCountOfVacantSlotsPerFloorPerType(parkingLotId);
    }

    @GetMapping("vacant/all/{parkingLotId}")
    public Iterable<SlotDTO> getAllVacantSlotsPerFloorForType(@PathVariable int parkingLotId) {
        return mapToListOfDTO(slotService.getAllVacantSlotsPerFloorPerType(parkingLotId));
    }

    @GetMapping("occupied/all/{parkingLotId}")
    public Iterable<SlotDTO> getAllOccupiedSlotsPerFloorForType(@PathVariable int parkingLotId) {
        return mapToListOfDTO(slotService.getAllOccupiedSlotsPerFloorPerType(parkingLotId));
    }

    @PostMapping("{parkingFloorId}/bulk")
    public Iterable<SlotDTO> addNewSlots(@PathVariable int parkingFloorId, @RequestBody List<SlotDTO> slotsDTO) {
        ParkingFloor parkingFloor = parkingFloorService.getParkingFloor(parkingFloorId);
        List<Slot> slots = slotsDTO.stream()
                .map(slotDTO -> Slot.builder()
                        .name(slotDTO.getName())
                        .slotStatus(slotDTO.getSlotStatus())
                        .slotType(slotDTO.getSlotType())
                        .parkingFloor(parkingFloor)
                        .build())
                .collect(Collectors.toList());
        return mapToListOfDTO(slotService.addNewSlots(slots));
    }

    private Iterable<SlotDTO> mapToListOfDTO(Iterable<Slot> slots) {
        return StreamSupport.stream(slots.spliterator(), false)
                .map(entity -> modelMapper.map(entity, SlotDTO.class))
                .collect(Collectors.toList());
    }

    @DeleteMapping
    public void deleteMultipleSlots(@RequestBody Iterable<Integer> ids) {
        slotService.deleteSlotsById(ids);
    }
}

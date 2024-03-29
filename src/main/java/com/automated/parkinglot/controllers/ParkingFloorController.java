package com.automated.parkinglot.controllers;

import com.automated.parkinglot.dto.ParkingFloorDTO;
import com.automated.parkinglot.models.application.parking.ParkingFloor;
import com.automated.parkinglot.service.IParkingFloorService;
import com.automated.parkinglot.service.IParkingLotService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/parkingfloor")
public class ParkingFloorController {

    private final IParkingFloorService parkingFloorService;
    private final IParkingLotService parkingLotService;
    private final ModelMapper modelMapper;

    @GetMapping("all/{parkingLotId}")
    public List<ParkingFloorDTO> getAllParkingFloors(@PathVariable int parkingLotId) {
        return parkingFloorService.getAllParkingFloorsByParkingLot(parkingLotId).stream()
                .map(entity -> modelMapper.map(entity, ParkingFloorDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ParkingFloorDTO getParkingFloor(@PathVariable int id) {
        return modelMapper.map(parkingFloorService.getParkingFloor(id), ParkingFloorDTO.class);
    }

    @PostMapping("{parkingLotId}")
    public ParkingFloorDTO saveNewParkingFloor(
            @PathVariable int parkingLotId, @RequestBody ParkingFloorDTO parkingFloorDTO) {
        var parkingLot = parkingLotService.getParkingLot(parkingLotId);
        var newParkingFloor =
                ParkingFloor.builder()
                        .parkingLot(parkingLot)
                        .name(parkingFloorDTO.getName())
                        .totalSlots(parkingFloorDTO.getTotalSlots())
                        .build();
        return modelMapper.map(
                parkingFloorService.addNewParkingFloor(newParkingFloor), ParkingFloorDTO.class);
    }

    @PutMapping
    public ParkingFloorDTO updateParkingFloor(@RequestBody ParkingFloorDTO parkingFloorDTO) {
        return modelMapper.map(
                parkingFloorService.updateParkingFloor(
                        modelMapper.map(parkingFloorDTO, ParkingFloor.class)),
                ParkingFloorDTO.class);
    }

    @DeleteMapping("{id}")
    public void deleteParkingFloor(@PathVariable int id) {
        parkingFloorService.deleteParkingFloor(id);
    }

    @PostMapping("{parkingLotId}/bulk")
    public Iterable<ParkingFloorDTO> saveNewParkingFloors(@PathVariable int parkingLotId,
                                                          @RequestBody List<ParkingFloorDTO> parkingFloorsDTO) {
        var parkingLot = parkingLotService.getParkingLot(parkingLotId);
        List<ParkingFloor> parkingFloors = parkingFloorsDTO.stream()
                .map(parkingFloorDTO -> ParkingFloor.builder()
                        .parkingLot(parkingLot)
                        .name(parkingFloorDTO.getName())
                        .totalSlots(parkingFloorDTO.getTotalSlots())
                        .build())
                .collect(Collectors.toList());
        return mapToListOfDTO(parkingFloorService.addNewParkingFloors(parkingLot, parkingFloors));
    }

    private Iterable<ParkingFloorDTO> mapToListOfDTO(Iterable<ParkingFloor> parkingFloors) {
        return StreamSupport.stream(parkingFloors.spliterator(), false)
                .map(parkingFloor -> modelMapper.map(parkingFloor, ParkingFloorDTO.class))
                .collect(Collectors.toList());
    }
}

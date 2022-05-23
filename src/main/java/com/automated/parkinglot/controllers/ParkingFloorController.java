package com.automated.parkinglot.controllers;

import com.automated.parkinglot.dto.ParkingFloorDTO;
import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.service.IParkingFloorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/parkingfloor")
public class ParkingFloorController {

    private final IParkingFloorService parkingFloorService;
    private final ModelMapper modelMapper;

    @GetMapping("all/{parkingLotId}")
    public List<ParkingFloorDTO> getAllParkingFloors(@PathVariable int parkingLotId) {
        return parkingFloorService.getAllParkingFloorsByParkingLot(parkingLotId).stream()
                .map(entity -> modelMapper.map(entity, ParkingFloorDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ParkingFloorDTO getParkingFloor(@PathVariable int id) {
        return modelMapper.map(parkingFloorService.getParkingFloor(id), ParkingFloorDTO.class);
    }

    @PostMapping
    public ParkingFloorDTO saveNewParkingFloor(@RequestBody ParkingFloorDTO parkingFloorDTO) {
        return modelMapper.map(
                parkingFloorService.addNewParkingFloor(modelMapper.map(parkingFloorDTO, ParkingFloor.class)),
                ParkingFloorDTO.class
        );
    }

    @PutMapping("{id}")
    public ParkingFloorDTO updateParkingFloor(@PathVariable int id, @RequestBody ParkingFloorDTO parkingFloorDTO) {
        return modelMapper.map(
                parkingFloorService.updateParkingFloor(id, modelMapper.map(parkingFloorDTO, ParkingFloor.class)),
                ParkingFloorDTO.class
        );
    }

    @DeleteMapping("{id}")
    public void deleteParkingFloor(@PathVariable int id) {
        parkingFloorService.deleteParkingFloor(id);
    }
}

package com.automated.parkinglot.controllers;


import com.automated.parkinglot.dto.ParkingLotDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import com.automated.parkinglot.service.IParkingLotService;
import com.automated.parkinglot.models.parking.ParkingLot;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController @RequestMapping("api/v1/parkinglot")
public class ParkingLotController {

    private final IParkingLotService parkingLotService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<ParkingLotDTO> getAllParkingLots() {
        return parkingLotService.getAllParkingLots().stream()
                .map(entity -> modelMapper.map(entity, ParkingLotDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ParkingLotDTO getParkingLot(@PathVariable int id) {
        return modelMapper.map(parkingLotService.getParkingLot(id), ParkingLotDTO.class);
    }

    @PostMapping
    public ParkingLotDTO addParkingLot(@RequestBody ParkingLotDTO parkingLotDTO) {
        return modelMapper.map(
                parkingLotService.addNewParkingLot(modelMapper.map(parkingLotDTO, ParkingLot.class)),
                ParkingLotDTO.class
        );
    }

    @PutMapping("{id}")
    public ParkingLotDTO updateParkingLot(@PathVariable int id, @RequestBody ParkingLotDTO parkingLotDTO) {
        return modelMapper.map(
                parkingLotService.updateParkingLot(id, modelMapper.map(parkingLotDTO, ParkingLot.class)),
                ParkingLotDTO.class
        );
    }

    @DeleteMapping("{id}")
    public void deleteParkingLot(@PathVariable int id) {
        parkingLotService.deleteParkingLot(id);
    }
}

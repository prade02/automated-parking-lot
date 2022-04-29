package com.automated.parkinglot.controllers;

import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.service.IParkingFloorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/parkingfloor")
public class ParkingFloorController {

    private final IParkingFloorService parkingFloorService;

    @GetMapping("all/{parkingLotId}")
    public List<ParkingFloor> getAllParkingFloors(@PathVariable int parkingLotId) {
        return parkingFloorService.getAllParkingFloorsByParkingLot(parkingLotId);
    }

    @GetMapping("{id}")
    public ParkingFloor getParkingFloor(@PathVariable int id) {
        return parkingFloorService.getParkingFloor(id);
    }

    @PostMapping
    public ParkingFloor saveNewParkingFloor(@RequestBody ParkingFloor parkingFloor) {
        return parkingFloorService.addNewParkingFloor(parkingFloor);
    }

    @PutMapping("{id}")
    public ParkingFloor updateParkingFloor(@PathVariable int id, @RequestBody ParkingFloor parkingFloor) {
        return parkingFloorService.updateParkingFloor(id, parkingFloor);
    }

    @DeleteMapping("{id}")
    public void deleteParkingFloor(@PathVariable int id) {
        parkingFloorService.deleteParkingFloor(id);
    }
}

package com.automated.parkinglot.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.automated.parkinglot.service.IParkingLotService;
import com.automated.parkinglot.models.parking.ParkingLot;

import java.util.List;

@AllArgsConstructor
@RestController @RequestMapping("api/v1/parkinglot")
public class ParkingLotController {

    private final IParkingLotService parkingLotService;

    @GetMapping
    public List<ParkingLot> getAllParkingLots() {
        return parkingLotService.getAllParkingLots();
    }

    @GetMapping("{id}")
    public ParkingLot getParkingLot(@PathVariable int id) {
        return parkingLotService.getParkingLot(id);
    }

    @PostMapping
    public ParkingLot addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotService.addNewParkingLot(parkingLot);
    }

    @PutMapping("{id}")
    public ParkingLot updateParkingLot(@PathVariable int id, @RequestBody ParkingLot parkingLot) {
        return parkingLotService.updateParkingLot(id, parkingLot);
    }

    @DeleteMapping("{id}")
    public void deleteParkingLot(@PathVariable int id) {
        parkingLotService.deleteParkingLot(id);
    }
}

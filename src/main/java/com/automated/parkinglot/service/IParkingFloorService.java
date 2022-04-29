package com.automated.parkinglot.service;

import com.automated.parkinglot.models.parking.ParkingFloor;

import java.util.List;

public interface IParkingFloorService {
    List<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId);
    ParkingFloor getParkingFloor(int id);
    ParkingFloor addNewParkingFloor(ParkingFloor parkingFloor);
    ParkingFloor updateParkingFloor(int id, ParkingFloor parkingFloor);
    void deleteParkingFloor(int id);
}

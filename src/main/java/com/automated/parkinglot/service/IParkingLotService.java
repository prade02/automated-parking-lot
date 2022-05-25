package com.automated.parkinglot.service;

import com.automated.parkinglot.models.parking.ParkingLot;

import java.util.List;

public interface IParkingLotService {
    List<ParkingLot> getAllParkingLots();

    ParkingLot getParkingLot(int id);

    ParkingLot addNewParkingLot(ParkingLot parkingLot);

    ParkingLot updateParkingLot(ParkingLot parkingLot);

    void deleteParkingLot(int id);
}

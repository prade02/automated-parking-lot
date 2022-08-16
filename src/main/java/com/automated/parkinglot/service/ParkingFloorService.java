package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.application.parking.ParkingFloor;
import com.automated.parkinglot.models.application.parking.ParkingLot;
import com.automated.parkinglot.repository.application.ParkingFloorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ParkingFloorService implements IParkingFloorService {

    private final ParkingFloorRepository parkingFloorRepository;

    @Override
    public List<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId) {
        var floors = new ArrayList<ParkingFloor>();
        parkingFloorRepository
                .getAllParkingFloorsByParkingLot(parkingLotId)
                .iterator()
                .forEachRemaining(floors::add);
        return floors;
    }

    @Override
    public ParkingFloor getParkingFloor(final int id) {
        var optionalParkingFloor = parkingFloorRepository.findById(id);
        if (optionalParkingFloor.isEmpty()) throw new InvalidRequestException("Given Id is not found");

        return optionalParkingFloor.get();
    }

    @Override
    public ParkingFloor addNewParkingFloor(final ParkingFloor parkingFloor) {
        if (!canAddNewFloor(parkingFloor.getParkingLot()))
            throw new InvalidRequestException("No new floors can be added to the parking lot");
        parkingFloor.setName(
                String.format("%s_%s", parkingFloor.getParkingLot().getName(), parkingFloor.getName()));
        return parkingFloorRepository.save(parkingFloor);
    }

    @Override
    public ParkingFloor updateParkingFloor(final ParkingFloor parkingFloor) {
        return parkingFloorRepository.save(parkingFloor);
    }

    @Override
    public void deleteParkingFloor(final int id) {
        if (parkingFloorRepository.findById(id).isEmpty())
            throw new InvalidRequestException("Given Id is not found");
        parkingFloorRepository.deleteById(id);
    }

    private boolean canAddNewFloor(ParkingLot parkingLot) {
        return parkingLot.getTotalFloors()
                > getAllParkingFloorsByParkingLot(parkingLot.getParkingLotId()).size();
    }
}

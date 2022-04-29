package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.repository.ParkingFloorRepository;
import com.automated.parkinglot.repository.ParkingLotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ParkingFloorService implements IParkingFloorService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingFloorRepository parkingFloorRepository;

    @Override
    public List<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId) {
        var floors = new ArrayList<ParkingFloor>();
        parkingFloorRepository.getAllParkingFloorsByParkingLot(parkingLotId).iterator().forEachRemaining(floors::add);
        return floors;
    }

    @Override
    public ParkingFloor getParkingFloor(final int id) {
        var optionalParkingFloor = parkingFloorRepository.findById(id);
        if (optionalParkingFloor.isEmpty())
            throw new InvalidRequestException("Given Id is not found");

        return optionalParkingFloor.get();
    }

    @Override
    public ParkingFloor addNewParkingFloor(final ParkingFloor parkingFloor) {
        var parkingLotName = parkingLotRepository.findById(parkingFloor.getParkingLot());
        if (parkingLotName.isEmpty())
            throw new InvalidRequestException("Invalid Parking Lot");

        parkingFloor.setName(String.format("%s_%s", parkingLotName.get().getName(), parkingFloor.getName()));
        return parkingFloorRepository.save(parkingFloor);
    }

    @Override
    public ParkingFloor updateParkingFloor(final int id, final ParkingFloor parkingFloor) {
        if (id != parkingFloor.getParkingFloorId())
            throw new InvalidRequestException("Given Id and Id of entity does not match");
        else if (parkingFloorRepository.findById(id).isEmpty())
            throw new InvalidRequestException("Given Id not found");

        return parkingFloorRepository.save(parkingFloor);
    }

    @Override
    public void deleteParkingFloor(final int id) {
        if (parkingFloorRepository.findById(id).isEmpty())
            throw new InvalidRequestException("Given Id is not found");
        parkingFloorRepository.deleteById(id);
    }
}

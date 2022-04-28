package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.parking.ParkingLot;
import com.automated.parkinglot.repository.ParkingLotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingLotService implements IParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    @Override
    public List<ParkingLot> getAllParkingLots() {
        final var parkingLots = new ArrayList<ParkingLot>();
        parkingLotRepository.findAll().iterator().forEachRemaining(parkingLots::add);
        return parkingLots;
    }

    @Override
    public ParkingLot getParkingLot(final int id) {
        final var optionalParkingLot = parkingLotRepository.findById(id);
        if (optionalParkingLot.isEmpty())
            throw new InvalidRequestException("Given Id not found");
        return optionalParkingLot.get();
    }

    @Override
    public ParkingLot addNewParkingLot(final ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public ParkingLot updateParkingLot(final int id, final ParkingLot parkingLot) {
        if (id != parkingLot.getParkingLotId())
            throw new InvalidRequestException("Id passed and Id in entity does not match");
        else if (parkingLotRepository.findById(id).isEmpty())
            throw new InvalidRequestException("Id not found");

        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public void deleteParkingLot(final int id) {
        final var optionalParkingLot = parkingLotRepository.findById(id);
        if (optionalParkingLot.isEmpty())
            throw new InvalidRequestException("Id not found");

        parkingLotRepository.delete(optionalParkingLot.get());
    }
}

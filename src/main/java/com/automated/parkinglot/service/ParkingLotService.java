package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.parking.ParkingLot;
import com.automated.parkinglot.repository.application.ParkingLotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ParkingLotService implements IParkingLotService {

  private final ParkingLotRepository parkingLotRepository;

  @Override
  public List<ParkingLot> getAllParkingLots() {
    return parkingLotRepository.findAll();
  }

  @Override
  public ParkingLot getParkingLot(final int id) {
    final var optionalParkingLot = parkingLotRepository.findById(id);
    if (optionalParkingLot.isEmpty()) throw new InvalidRequestException("Given Id not found");
    return optionalParkingLot.get();
  }

  @Override
  public ParkingLot addNewParkingLot(final ParkingLot parkingLot) {
    return parkingLotRepository.save(parkingLot);
  }

  @Override
  public ParkingLot updateParkingLot(final ParkingLot parkingLot) {
    if (parkingLotRepository.findById(parkingLot.getParkingLotId()).isEmpty())
      throw new InvalidRequestException("Id not found");

    return parkingLotRepository.save(parkingLot);
  }

  @Override
  public void deleteParkingLot(final int id) {
    parkingLotRepository.delete(getParkingLot(id));
  }
}

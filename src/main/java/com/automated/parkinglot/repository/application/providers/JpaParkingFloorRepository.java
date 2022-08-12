package com.automated.parkinglot.repository.application.providers;

import com.automated.parkinglot.models.application.parking.ParkingFloor;
import com.automated.parkinglot.models.application.parking.ParkingFloor_;
import com.automated.parkinglot.repository.application.ParkingFloorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class JpaParkingFloorRepository extends SimpleJpaRepository<ParkingFloor, Integer>
    implements ParkingFloorRepository {

  public JpaParkingFloorRepository(EntityManager entityManager) {
    super(ParkingFloor.class, entityManager);
  }

  @Override
  public Iterable<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId) {
    return this.findAll(floorsOnGivenLot(parkingLotId));
  }

  private Specification<ParkingFloor> floorsOnGivenLot(int parkingLotId) {
    return (parkingFloor, query, builder) ->
        builder.equal(parkingFloor.get(ParkingFloor_.PARKING_LOT), parkingLotId);
  }
}

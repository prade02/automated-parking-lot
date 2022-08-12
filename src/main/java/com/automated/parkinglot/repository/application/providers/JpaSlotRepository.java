package com.automated.parkinglot.repository.application.providers;

import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.ParkingFloor_;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.models.application.parking.Slot_;
import com.automated.parkinglot.repository.application.SlotRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import java.util.Optional;

@Repository
public class JpaSlotRepository extends SimpleJpaRepository<Slot, Integer>
    implements SlotRepository {

  public JpaSlotRepository(EntityManager entityManager) {
    super(Slot.class, entityManager);
  }

  @Override
  public Iterable<Slot> findAllSlotsByParkingFloor(int parkingFloorId) {
    return this.findAll(
        (slot, query, builder) ->
            builder.equal(
                slot.get(Slot_.PARKING_FLOOR).get(ParkingFloor_.PARKING_FLOOR_ID), parkingFloorId));
  }

  @Override
  public Optional<Slot> findByName(String name) {
    return this.findAll((slot, query, builder) -> builder.equal(slot.get(Slot_.NAME), name))
        .stream()
        .findFirst();
  }

  @Override
  public Iterable<Slot> getAllSlotsForStatus(SlotStatus status, int parkingLotId) {
    return this.findAll((slot, query, builder) -> {
      Order nameOrderAsc = builder.asc(slot.get(Slot_.NAME));
      query.orderBy(nameOrderAsc);
      return builder.and(builder.equal(slot.get(Slot_.SLOT_STATUS), status), builder.equal(
              slot.get(Slot_.PARKING_FLOOR).get(ParkingFloor_.PARKING_LOT), parkingLotId));
    });
  }
}

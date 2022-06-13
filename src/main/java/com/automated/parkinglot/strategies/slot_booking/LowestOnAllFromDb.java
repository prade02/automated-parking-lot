package com.automated.parkinglot.strategies.slot_booking;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.ParkingFloor_;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.models.parking.Slot_;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

@Repository
@AllArgsConstructor
public class LowestOnAllFromDb implements SlotBookingStrategy {

  private final EntityManager entityManager;

  @Override
  @Lock(LockModeType.PESSIMISTIC_READ)
  public Slot bookSlot(int parkingLotId, GenericType slotType) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Slot> query = criteriaBuilder.createQuery(Slot.class);
    Root<Slot> slot = query.from(Slot.class);

    // set predicates/conditions - where clause
    Predicate parkingLotPredicate =
        criteriaBuilder.equal(slot.get(Slot_.PARKING_FLOOR).get(ParkingFloor_.PARKING_LOT), parkingLotId);
    Predicate slotStatusPredicate =
        criteriaBuilder.equal(slot.get(Slot_.SLOT_STATUS), SlotStatus.VACANT);
    Predicate slotTypePredicate = criteriaBuilder.equal(slot.get(Slot_.SLOT_TYPE), slotType);

    // set order by clause
    Order parkingFloorOrder = criteriaBuilder.asc(slot.get(Slot_.PARKING_FLOOR));
    Order slotOrder = criteriaBuilder.asc(slot.get(Slot_.SLOT_ID));

    // wire the clauses to the query
    query.where(parkingLotPredicate, slotStatusPredicate, slotTypePredicate);
    query.orderBy(parkingFloorOrder, slotOrder);

    return entityManager.createQuery(query).getResultStream().findFirst().orElse(null);
  }
}

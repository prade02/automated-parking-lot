package com.automated.parkinglot.strategies.slot_booking;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.QSlot;
import com.automated.parkinglot.models.parking.Slot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

@Repository
public class LowestOnAllFromDb implements SlotBookingStrategy {

  private final QSlot slot = QSlot.slot;
  private final JPAQueryFactory jpaQueryFactory;

  public LowestOnAllFromDb(EntityManager entityManager) {
    this.jpaQueryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  @Lock(LockModeType.PESSIMISTIC_READ)
  public Slot bookSlot(int parkingLotId, GenericType slotType) {
    return jpaQueryFactory
        .selectFrom(slot)
        .where(
            slot.parkingFloor
                .parkingLot
                .parkingLotId
                .eq(parkingLotId)
                .and(slot.slotStatus.eq(SlotStatus.VACANT))
                .and(slot.slotType.eq(slotType)))
        .orderBy(slot.parkingFloor.parkingFloorId.asc())
        .orderBy(slot.slotId.asc())
        .fetchFirst();
  }
}

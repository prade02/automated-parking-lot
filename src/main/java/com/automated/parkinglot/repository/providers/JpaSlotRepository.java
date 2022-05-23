package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.QParkingFloor;
import com.automated.parkinglot.models.parking.QSlot;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.repository.SlotRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import javax.persistence.EntityManager;

@Repository
public class JpaSlotRepository extends SimpleJpaRepository<Slot, Integer> implements SlotRepository {

    private final QSlot slot = QSlot.slot;
    private final QParkingFloor parkingFloor = QParkingFloor.parkingFloor;
    private final JPAQueryFactory jpaQueryFactory;

    public JpaSlotRepository(EntityManager entityManager) {
        super(Slot.class, entityManager);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Iterable<Slot> findAllSlotsByParkingFloor(int parkingFloorId) {
        return jpaQueryFactory.selectFrom(slot).where(slot.parkingFloor.eq(parkingFloorId)).fetch();
    }

    @Override
    public Optional<Slot> findByName(String name) {
        return Optional.of(jpaQueryFactory.selectFrom(slot).where(slot.name.eq(name)).fetchFirst());
    }

    @Override
    public Slot getAvailableSlot(int parkingLotId, GenericType slotType) {
        return jpaQueryFactory
                .selectFrom(slot)
                .join(parkingFloor)
                .on(slot.parkingFloor.eq(parkingFloor.parkingFloorId))
                .where(parkingFloor.parkingLot.eq(parkingLotId)
                               .and(slot.slotStatus.eq(SlotStatus.VACANT))
                               .and(slot.slotType.eq(slotType)))
                .orderBy(slot.name.asc())
                .fetchFirst();
    }

    @Override
    public Iterable<Slot> getAllSlotsForStatus(SlotStatus status, int parkingLotId) {
        return jpaQueryFactory
                .selectFrom(slot)
                .join(parkingFloor)
                .on(slot.parkingFloor.eq(parkingFloor.parkingFloorId))
                .where(parkingFloor.parkingLot.eq(parkingLotId).and(slot.slotStatus.eq(status)))
                .orderBy(slot.name.asc())
                .fetch();
    }
}

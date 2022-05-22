package com.automated.parkinglot.repository.providers;

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
    public Integer getAvailableSlot(int parkingLotId, String slotType) {
        // TODO: implement
        return null;
    }

    @Override
    public Iterable<Slot> getAllSlotsForStatus(String status, int parkingLotId) {
        // TODO: implement
        return null;
    }
}

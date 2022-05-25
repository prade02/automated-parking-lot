package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.models.parking.QParkingFloor;
import com.automated.parkinglot.repository.ParkingFloorRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class JpaParkingFloorRepository extends SimpleJpaRepository<ParkingFloor, Integer> implements ParkingFloorRepository {

    private final QParkingFloor parkingFloor = QParkingFloor.parkingFloor;
    private final JPAQueryFactory jpaQueryFactory;

    public JpaParkingFloorRepository(EntityManager entityManager) {
        super(ParkingFloor.class, entityManager);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Iterable<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId) {
        return jpaQueryFactory.selectFrom(parkingFloor)
                .where(parkingFloor.parkingLot.parkingLotId.eq(parkingLotId)).fetch();
    }
}

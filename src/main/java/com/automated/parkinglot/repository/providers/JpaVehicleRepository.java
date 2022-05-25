package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.vehicle.QVehicle;
import com.automated.parkinglot.models.vehicle.Vehicle;
import com.automated.parkinglot.repository.VehicleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class JpaVehicleRepository extends SimpleJpaRepository<Vehicle, Integer> implements VehicleRepository {

    private final QVehicle vehicle = QVehicle.vehicle;
    private final JPAQueryFactory jpaQueryFactory;

    public JpaVehicleRepository(EntityManager entityManager) {
        super(Vehicle.class, entityManager);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Vehicle> findLatestVehicleEntry(String registration) {
        return Optional.of(jpaQueryFactory.selectFrom(vehicle).
                where(vehicle.registrationNumber.eq(registration)).orderBy(vehicle.inTime.desc()).fetchFirst());
    }
}

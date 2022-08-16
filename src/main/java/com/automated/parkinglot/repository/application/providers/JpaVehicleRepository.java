package com.automated.parkinglot.repository.application.providers;

import com.automated.parkinglot.models.application.vehicle.Vehicle;
import com.automated.parkinglot.models.application.vehicle.Vehicle_;
import com.automated.parkinglot.repository.application.VehicleRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import java.util.Optional;

@Repository
public class JpaVehicleRepository extends SimpleJpaRepository<Vehicle, Integer>
        implements VehicleRepository {

    public JpaVehicleRepository(EntityManager entityManager) {
        super(Vehicle.class, entityManager);
    }

    @Override
    public Optional<Vehicle> findLatestVehicleEntry(String registration) {
        return this.findAll(
                        (vehicle, query, builder) -> {
                            Order inTimeOrderDesc = builder.desc(vehicle.get(Vehicle_.IN_TIME));
                            query.orderBy(inTimeOrderDesc);
                            return builder.equal(vehicle.get(Vehicle_.REGISTRATION_NUMBER), registration);
                        })
                .stream()
                .findFirst();
    }
}

package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Query(value = "SELECT * FROM vehicle WHERE registration_number = :registration ORDER BY in_time DESC LIMIT 1",
           nativeQuery = true)
    Optional<Vehicle> findLatestVehicleEntry(@Param("registration") String registration);
}

package com.automated.parkinglot.repository;

import com.automated.parkinglot.models.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
  Optional<Vehicle> findLatestVehicleEntry(String registration);
}

package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.vehicle.Vehicle;
import com.automated.parkinglot.repository.VehicleRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class JpaVehicleRepository extends SimpleJpaRepository<Vehicle, Integer>
    implements VehicleRepository {

  private final EntityManager entityManager;
  private final CriteriaBuilder criteriaBuilder;

  public JpaVehicleRepository(EntityManager entityManager) {
    super(Vehicle.class, entityManager);
    this.entityManager = entityManager;
    this.criteriaBuilder = entityManager.getCriteriaBuilder();
  }

  @Override
  public Optional<Vehicle> findLatestVehicleEntry(String registration) {
    CriteriaQuery<Vehicle> query = criteriaBuilder.createQuery(Vehicle.class);
    Root<Vehicle> vehicle = query.from(Vehicle.class);

    // where clause
    Predicate registrationPredicate =
        criteriaBuilder.equal(vehicle.get("registrationNumber"), registration);

    // order clause
    Order inTimeOrderDesc = criteriaBuilder.desc(vehicle.get("inTime"));

    // wire clause to query
    query.where(registrationPredicate);
    query.orderBy(inTimeOrderDesc);

    return entityManager.createQuery(query).getResultStream().findFirst();
  }
}

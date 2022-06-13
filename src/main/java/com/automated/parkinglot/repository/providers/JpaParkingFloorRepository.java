package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.models.parking.ParkingFloor_;
import com.automated.parkinglot.repository.ParkingFloorRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class JpaParkingFloorRepository extends SimpleJpaRepository<ParkingFloor, Integer>
    implements ParkingFloorRepository {

  private final EntityManager entityManager;
  private final CriteriaBuilder criteriaBuilder;

  public JpaParkingFloorRepository(EntityManager entityManager) {
    super(ParkingFloor.class, entityManager);
    this.entityManager = entityManager;
    this.criteriaBuilder = entityManager.getCriteriaBuilder();
  }

  @Override
  public Iterable<ParkingFloor> getAllParkingFloorsByParkingLot(int parkingLotId) {
    CriteriaQuery<ParkingFloor> query = criteriaBuilder.createQuery(ParkingFloor.class);
    Root<ParkingFloor> parkingFloor = query.from(ParkingFloor.class);

    // where clause
    Predicate parkingLotIdPredicate = criteriaBuilder.equal(parkingFloor.get(ParkingFloor_.PARKING_LOT), parkingLotId);

    // wire clause to query
    query.where(parkingLotIdPredicate);

    return entityManager.createQuery(query).getResultList();
  }
}

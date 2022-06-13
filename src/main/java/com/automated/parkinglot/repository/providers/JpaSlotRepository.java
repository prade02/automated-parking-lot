package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.repository.SlotRepository;
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
public class JpaSlotRepository extends SimpleJpaRepository<Slot, Integer>
    implements SlotRepository {

  private final EntityManager entityManager;
  private final CriteriaBuilder criteriaBuilder;

  public JpaSlotRepository(EntityManager entityManager) {
    super(Slot.class, entityManager);
    this.entityManager = entityManager;
    this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
  }

  @Override
  public Iterable<Slot> findAllSlotsByParkingFloor(int parkingFloorId) {
    CriteriaQuery<Slot> query = criteriaBuilder.createQuery(Slot.class);
    Root<Slot> slot = query.from(Slot.class);

    // where clause
    Predicate parkingFloorPredicate =
        criteriaBuilder.equal(slot.get("parkingFloor").get("parkingFloorId"), parkingFloorId);

    // wire predicate to query
    query.where(parkingFloorPredicate);

    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public Optional<Slot> findByName(String name) {
    CriteriaQuery<Slot> query = criteriaBuilder.createQuery(Slot.class);
    Root<Slot> slot = query.from(Slot.class);

    // where clause
    Predicate namePredicate = criteriaBuilder.equal(slot.get("name"), name);

    // wire predicate to query
    query.where(namePredicate);

    return entityManager.createQuery(query).getResultStream().findFirst();
  }

  @Override
  public Iterable<Slot> getAllSlotsForStatus(SlotStatus status, int parkingLotId) {
    CriteriaQuery<Slot> query = criteriaBuilder.createQuery(Slot.class);
    Root<Slot> slot = query.from(Slot.class);

    // where clause
    Predicate parkingLotPredicate =
        criteriaBuilder.equal(slot.get("parkingFloor").get("parkingLot"), parkingLotId);
    Predicate statusPredicate = criteriaBuilder.equal(slot.get("slotStatus"), status);

    // order clause
    Order nameOrderAsc = criteriaBuilder.asc(slot.get("name"));

    // wire clauses to query
    query.where(parkingLotPredicate, statusPredicate);
    query.orderBy(nameOrderAsc);

    return entityManager.createQuery(query).getResultList();
  }
}

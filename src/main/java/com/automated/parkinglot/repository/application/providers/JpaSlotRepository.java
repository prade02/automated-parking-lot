package com.automated.parkinglot.repository.application.providers;

import com.automated.parkinglot.dto.PagedContents;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.ParkingFloor_;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.models.application.parking.Slot_;
import com.automated.parkinglot.repository.application.SlotRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaSlotRepository extends SimpleJpaRepository<Slot, Integer>
        implements SlotRepository {

    public JpaSlotRepository(EntityManager entityManager) {
        super(Slot.class, entityManager);
    }

    @Override
    public PagedContents<Slot> findAllSlotsByParkingFloor(int parkingFloorId, int pageNumber, int pageSize,
                                                          boolean setPageInfo) {
        Specification<Slot> specification = (slot, query, builder) ->
                builder.equal(slot.get(Slot_.PARKING_FLOOR), parkingFloorId);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Slot_.NAME).ascending());
        return setPageInfo ? getContentsWithPageInfo(specification, pageRequest) :
                getOnlyContentsWithoutPageInfo(specification, pageRequest);
    }

    private PagedContents<Slot> getContentsWithPageInfo(Specification<Slot> specification, Pageable pageRequest) {
        var page = this.findAll(specification, pageRequest);
        // set currentPageNumber in DTO with `+1` to make it non-zero index based
        return buildPagedContents(page.getContent(), page.getTotalPages(), page.getNumber() + 1);
    }

    private PagedContents<Slot> getOnlyContentsWithoutPageInfo(Specification<Slot> specification, Pageable pageRequest) {
        var slots = this.getQuery(specification, pageRequest)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
        return buildPagedContents(slots, -1, -1);
    }

    private PagedContents<Slot> buildPagedContents(List<Slot> slots, int totalPages, int currentPageNumber) {
        return PagedContents.<Slot>builder()
                .contents(slots)
                .totalPages(totalPages)
                .currentPageNumber(currentPageNumber)
                .totalElements(slots.size())
                .build();
    }

    @Override
    public Optional<Slot> findByName(String name) {
        return this.findAll((slot, query, builder) -> builder.equal(slot.get(Slot_.NAME), name))
                .stream()
                .findFirst();
    }

    @Override
    public Iterable<Slot> getAllSlotsForStatus(SlotStatus status, int parkingLotId) {
        return this.findAll((slot, query, builder) -> {
            Order nameOrderAsc = builder.asc(slot.get(Slot_.NAME));
            query.orderBy(nameOrderAsc);
            return builder.and(builder.equal(slot.get(Slot_.SLOT_STATUS), status), builder.equal(
                    slot.get(Slot_.PARKING_FLOOR).get(ParkingFloor_.PARKING_LOT), parkingLotId));
        });
    }
}

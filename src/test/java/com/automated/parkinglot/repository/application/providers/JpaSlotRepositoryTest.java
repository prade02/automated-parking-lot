package com.automated.parkinglot.repository.application.providers;

import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.ParkingFloor;
import com.automated.parkinglot.models.application.parking.ParkingLot;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.repository.application.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({JpaSlotRepository.class, JpaParkingFloorRepository.class})
class JpaSlotRepositoryTest {

  private static final int SLOT_INITIAL_SIZE = 4;
  private static final int SLOT_VACANT_COUNT = 3;
  private static final int SLOT_OCCUPIED_COUNT = 1;
  private static final String SLOT_NAME_TO_FIND = "LOT1_1_2";

  @Autowired private JpaSlotRepository jpaSlotRepository;
  @Autowired private ParkingLotRepository parkingLotRepository;
  @Autowired private JpaParkingFloorRepository jpaParkingFloorRepository;

  private ParkingFloor parkingFloor;

  private ParkingFloor generateParkingFloor() {
    var parkingLot = new ParkingLot();
    var parkingFloor = ParkingFloor.builder().parkingLot(parkingLot).build();
    parkingLotRepository.save(parkingLot);
    jpaParkingFloorRepository.save(parkingFloor);
    return parkingFloor;
  }

  private void addSlotsForBasicTest(ParkingFloor parkingFloor) {
    addSlots(1, GenericType.CAR, SlotStatus.VACANT, "LOT1_1_1", parkingFloor);
    addSlots(2, GenericType.CAR, SlotStatus.VACANT, "LOT1_1_2", parkingFloor);
    addSlots(3, GenericType.BIKE, SlotStatus.VACANT, "LOT1_1_3", parkingFloor);
    addSlots(4, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_1_4", parkingFloor);
  }

  private void addSlots(
      int slotId,
      GenericType slotType,
      SlotStatus slotStatus,
      String name,
      ParkingFloor parkingFloor) {
    jpaSlotRepository.save(
        Slot.builder()
            .slotId(slotId)
            .slotType(slotType)
            .slotStatus(slotStatus)
            .name(name)
            .parkingFloor(parkingFloor)
            .build());
  }

  @BeforeEach
  void setUp() {
    // given
    parkingFloor = generateParkingFloor();
    addSlotsForBasicTest(parkingFloor);
  }

  @Test
  void findAll_WhenMultipleSlotsPresent() {
    // when
    var slots = jpaSlotRepository.findAllSlotsByParkingFloor(parkingFloor.getParkingFloorId());

    // then
    assertThat(StreamSupport.stream(slots.spliterator(), false).count())
        .isEqualTo(SLOT_INITIAL_SIZE);
  }

  @Test
  void findAll_WhenNoSlotsPresent() {
    // given
    jpaSlotRepository.deleteAll();

    // when
    var slots = jpaSlotRepository.findAllSlotsByParkingFloor(parkingFloor.getParkingFloorId());

    // then
    assertThat(StreamSupport.stream(slots.spliterator(), false).count()).isEqualTo(0);
  }

  @Test
  void findByName() {
    // when
    var optionalSlot = jpaSlotRepository.findByName(SLOT_NAME_TO_FIND);

    // then
    assertThat(optionalSlot.isEmpty()).isFalse();
    assertThat(optionalSlot.get().getName()).isEqualTo(SLOT_NAME_TO_FIND);
  }

  @Test
  void getAllSlots_ForOccupied() {
    // when
    var slots =
        jpaSlotRepository.getAllSlotsForStatus(
            SlotStatus.OCCUPIED, parkingFloor.getParkingLot().getParkingLotId());
    var noOfSlotsReturned = StreamSupport.stream(slots.spliterator(), false).count();

    // then
    assertThat(noOfSlotsReturned).isEqualTo(SLOT_OCCUPIED_COUNT);
  }

  @Test
  void getAllSlots_ForVacant() {
    // when
    var slots =
        jpaSlotRepository.getAllSlotsForStatus(
            SlotStatus.VACANT, parkingFloor.getParkingLot().getParkingLotId());
    var noOfSlotsReturned = StreamSupport.stream(slots.spliterator(), false).count();

    // then
    assertThat(noOfSlotsReturned).isEqualTo(SLOT_VACANT_COUNT);
  }
}

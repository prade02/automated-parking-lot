package com.automated.parkinglot.strategies.slot_booking;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.models.parking.ParkingLot;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.repository.ParkingLotRepository;
import com.automated.parkinglot.repository.providers.JpaParkingFloorRepository;
import com.automated.parkinglot.repository.providers.JpaSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({LowestOnAllFromDb.class, JpaParkingFloorRepository.class, JpaSlotRepository.class})
class LowestOnAllFromDbTest {

  private static final String LOWEST_SLOT_FOR_CAR = "LOT1_5_1";
  private static final String LOWEST_SLOT_FOR_BIKE = "LOT1_1_4";
  private static final String LOWEST_SLOT_FOR_TRUCK = "LOT1_14_4";

  private ParkingLot parkingLot;
  @Autowired private LowestOnAllFromDb lowestOnAllFromDb;

  @Autowired private ParkingLotRepository parkingLotRepository;
  @Autowired private JpaSlotRepository jpaSlotRepository;
  @Autowired private JpaParkingFloorRepository jpaParkingFloorRepository;

  private ParkingFloor generateParkingFloor(ParkingLot parkingLot) {
    var parkingFloor = ParkingFloor.builder().parkingLot(parkingLot).build();
    parkingLotRepository.save(parkingLot);
    jpaParkingFloorRepository.save(parkingFloor);
    return parkingFloor;
  }

  private void addSlotsForBasicTest(
      ParkingFloor parkingFloorFirst,
      ParkingFloor parkingFloorSecond,
      ParkingFloor parkingFloorThird) {
    // first floor
    addSlots(1, GenericType.CAR, SlotStatus.OCCUPIED, "LOT1_1_1", parkingFloorFirst);
    addSlots(2, GenericType.CAR, SlotStatus.OCCUPIED, "LOT1_1_2", parkingFloorFirst);
    addSlots(3, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_1_3", parkingFloorFirst);
    addSlots(4, GenericType.BIKE, SlotStatus.VACANT, "LOT1_1_4", parkingFloorFirst);

    // second floor
    addSlots(5, GenericType.CAR, SlotStatus.VACANT, "LOT1_5_1", parkingFloorSecond);
    addSlots(6, GenericType.CAR, SlotStatus.VACANT, "LOT1_6_2", parkingFloorSecond);
    addSlots(7, GenericType.BIKE, SlotStatus.VACANT, "LOT1_7_3", parkingFloorSecond);
    addSlots(8, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_8_4", parkingFloorSecond);

    // third floor
    addSlots(9, GenericType.CAR, SlotStatus.VACANT, "LOT1_9_1", parkingFloorThird);
    addSlots(10, GenericType.CAR, SlotStatus.VACANT, "LOT1_10_2", parkingFloorThird);
    addSlots(11, GenericType.BIKE, SlotStatus.VACANT, "LOT1_11_3", parkingFloorThird);
    addSlots(12, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_12_4", parkingFloorThird);
    addSlots(13, GenericType.TRUCK, SlotStatus.OCCUPIED, "LOT1_13_4", parkingFloorThird);
    addSlots(14, GenericType.TRUCK, SlotStatus.VACANT, "LOT1_14_4", parkingFloorThird);
  }

  private void addSlotsForNoAvailableSlotTest(
      ParkingFloor parkingFloorFirst, ParkingFloor parkingFloorSecond) {
    // first floor
    addSlots(1, GenericType.CAR, SlotStatus.OCCUPIED, "LOT1_1_1", parkingFloorFirst);
    addSlots(2, GenericType.TRUCK, SlotStatus.OCCUPIED, "LOT1_1_2", parkingFloorFirst);
    addSlots(3, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_1_3", parkingFloorFirst);
    addSlots(4, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_1_4", parkingFloorFirst);

    // second floor
    addSlots(5, GenericType.CAR, SlotStatus.OCCUPIED, "LOT1_5_1", parkingFloorSecond);
    addSlots(6, GenericType.CAR, SlotStatus.OCCUPIED, "LOT1_6_2", parkingFloorSecond);
    addSlots(7, GenericType.TRUCK, SlotStatus.OCCUPIED, "LOT1_7_3", parkingFloorSecond);
    addSlots(8, GenericType.BIKE, SlotStatus.OCCUPIED, "LOT1_8_4", parkingFloorSecond);
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
    var parkingLot = new ParkingLot();
    var parkingFloorFirst = generateParkingFloor(parkingLot);
    var parkingFloorSecond = generateParkingFloor(parkingLot);
    var parkingFloorThird = generateParkingFloor(parkingLot);
    addSlotsForBasicTest(parkingFloorFirst, parkingFloorSecond, parkingFloorThird);
    this.parkingLot = parkingFloorFirst.getParkingLot();
  }

  private void setUpForAllSlotsOccupied() {
    // given
    jpaSlotRepository.deleteAll();
    var parkingLot = new ParkingLot();
    var parkingFloorFirst = generateParkingFloor(parkingLot);
    var parkingFloorSecond = generateParkingFloor(parkingLot);
    addSlotsForNoAvailableSlotTest(parkingFloorFirst, parkingFloorSecond);
    this.parkingLot = parkingFloorFirst.getParkingLot();
  }

  @Test
  void tryBookSlotWhenAvailable() {
    // when
    var slotsForCar = lowestOnAllFromDb.bookSlot(parkingLot.getParkingLotId(), GenericType.CAR);
    var slotsForBike = lowestOnAllFromDb.bookSlot(parkingLot.getParkingLotId(), GenericType.BIKE);
    var slotsForTruck = lowestOnAllFromDb.bookSlot(parkingLot.getParkingLotId(), GenericType.TRUCK);

    // then
    assertThat(slotsForCar.getName()).isEqualTo(LOWEST_SLOT_FOR_CAR);
    assertThat(slotsForBike.getName()).isEqualTo(LOWEST_SLOT_FOR_BIKE);
    assertThat(slotsForTruck.getName()).isEqualTo(LOWEST_SLOT_FOR_TRUCK);
  }

  @Test
  void tryBookSlotWhenNoSlotAvailable() {
    // given
    setUpForAllSlotsOccupied();

    // when
    var slotsForCar = lowestOnAllFromDb.bookSlot(parkingLot.getParkingLotId(), GenericType.CAR);
    var slotsForBike = lowestOnAllFromDb.bookSlot(parkingLot.getParkingLotId(), GenericType.BIKE);
    var slotsForTruck = lowestOnAllFromDb.bookSlot(parkingLot.getParkingLotId(), GenericType.TRUCK);

    // then
    assertThat(slotsForCar).isNull();
    assertThat(slotsForBike).isNull();
    assertThat(slotsForTruck).isNull();
  }
}

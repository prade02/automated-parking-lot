package com.automated.parkinglot.repository.providers;

import com.automated.parkinglot.models.parking.ParkingFloor;
import com.automated.parkinglot.models.parking.ParkingLot;
import com.automated.parkinglot.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(JpaParkingFloorRepository.class)
class JpaParkingFloorRepositoryTest {

  @Autowired private ParkingLotRepository parkingLotRepository;
  @Autowired private JpaParkingFloorRepository jpaParkingFloorRepository;

  private ParkingLot parkingLot;
  private static final String PARKING_LOT_NAME_ONE = "LOT1_1";
  private static final String PARKING_LOT_NAME_TWO = "LOT1_2";
  private static final String PARKING_LOT_NAME_THREE = "LOT1_3";

  @BeforeEach
  void setUp() {
    // given
    var parkingLot = new ParkingLot();
    var parkingFloorOne =
        ParkingFloor.builder().parkingLot(parkingLot).name(PARKING_LOT_NAME_ONE).build();
    var parkingFloorTwo =
        ParkingFloor.builder().parkingLot(parkingLot).name(PARKING_LOT_NAME_TWO).build();
    var parkingFloorThree =
        ParkingFloor.builder().parkingLot(parkingLot).name(PARKING_LOT_NAME_THREE).build();

    this.parkingLot = parkingLotRepository.save(parkingLot);
    jpaParkingFloorRepository.save(parkingFloorOne);
    jpaParkingFloorRepository.save(parkingFloorTwo);
    jpaParkingFloorRepository.save(parkingFloorThree);
  }

  @Test
  void getAllParkingFloorsByParkingLot_WhenMultipleFloorsAvailable() {
    // when
    Iterable<ParkingFloor> allParkingFloorsByParkingLot =
        jpaParkingFloorRepository.getAllParkingFloorsByParkingLot(
            this.parkingLot.getParkingLotId());

    // then
    assertThat(allParkingFloorsByParkingLot).isNotNull();
    long countOfFloors =
        StreamSupport.stream(allParkingFloorsByParkingLot.spliterator(), false).count();
    assertThat(countOfFloors).isGreaterThan(0);
  }

  @Test
  void getAllParkingFloorsByParkingLot_WhenNoFloorsAvailable() {
    // given
    jpaParkingFloorRepository.deleteAll();

    // when
    Iterable<ParkingFloor> allParkingFloorsByParkingLot =
        jpaParkingFloorRepository.getAllParkingFloorsByParkingLot(
            this.parkingLot.getParkingLotId());

    // then
    assertThat(allParkingFloorsByParkingLot).isNotNull();
    long countOfFloors =
        StreamSupport.stream(allParkingFloorsByParkingLot.spliterator(), false).count();
    assertThat(countOfFloors).isEqualTo(0);
  }

  @Test
  void getAllParkingFloorsByParkingLot_ForInvalidParkingLot() {
    // given
    int parkingLot = 0;

    // when
    Iterable<ParkingFloor> allParkingFloorsByParkingLot =
        jpaParkingFloorRepository.getAllParkingFloorsByParkingLot(parkingLot);

    // then
    assertThat(allParkingFloorsByParkingLot).isNotNull();
    long countOfFloors =
        StreamSupport.stream(allParkingFloorsByParkingLot.spliterator(), false).count();
    assertThat(countOfFloors).isEqualTo(0);
  }
}

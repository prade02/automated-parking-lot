package com.automated.parkinglot.repository.application.providers;

import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.ParkingFloor;
import com.automated.parkinglot.models.application.parking.ParkingLot;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.models.application.vehicle.Vehicle;
import com.automated.parkinglot.repository.application.ParkingLotRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({JpaVehicleRepository.class, JpaSlotRepository.class, JpaParkingFloorRepository.class})
class JpaVehicleRepositoryTest {

    // SLOT NAMES
    private static final String SLOT_NAME_ONE = "LOT1_1_1";
    private static final String SLOT_NAME_TWO = "LOT1_1_2";
    private static final String SLOT_NAME_THREE = "LOT1_1_3";
    private static final String SLOT_NAME_FOUR = "LOT1_1_4";

    // REGISTRATION NUMBERS
    private static final String UNKNOWN_REGISTRATION_NUMBER = "TN-O1-BB-4235";
    private static final String KNOWN_REGISTRATION_NUMBER_ONE = "TN-O1-BB-4233";
    private static final String KNOWN_REGISTRATION_NUMBER_TWO = "TN-O1-BB-4234";
    private static final String KNOWN_REGISTRATION_NUMBER_THREE = "TN-O1-AR-7287";
    private static final String KNOWN_REGISTRATION_NUMBER_FOUR = "TN-O1-BA-5187";

    // IN/OUT TIME
    private static final String VEHICLE_ONE_IN_DATE = "2022-05-02 12:04:38";
    private static final String VEHICLE_ONE_OUT_DATE = "2022-05-02 12:40:38";
    private static final String VEHICLE_TWO_IN_DATE = "2022-05-07 16:55:12 ";
    private static final String VEHICLE_TWO_OUT_DATE = "2022-05-07 16:59:12 ";
    private static final String VEHICLE_THREE_IN_DATE = "2022-05-23 12:27:40";
    private static final String VEHICLE_THREE_OUT_DATE = "2022-05-23 12:47:40";
    private static final String VEHICLE_FOUR_IN_DATE = "2022-05-25 15:52:36";
    private static final String VEHICLE_FOUR_OUT_DATE = "2022-05-25 15:58:36";

    // SAME VEHICLE WITH DIFFERENT ENTRY TIME
    private static final String VEHICLE_ONE_IN_DATE_LATER = "2022-05-03 12:04:38";
    private static final String VEHICLE_ONE_OUT_DATE_LATER = "2022-05-03 12:40:38";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private JpaSlotRepository jpaSlotRepository;
    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private JpaParkingFloorRepository jpaParkingFloorRepository;
    private Slot slotForReUse;

    @BeforeEach
    void setUp() {
        // given
        var parkingLot = new ParkingLot();
        var parkingFloor = ParkingFloor.builder().parkingLot(parkingLot).build();
        var slotOne = slotForReUse = createSlot(SLOT_NAME_ONE, GenericType.CAR, parkingFloor);
        var slotTwo = createSlot(SLOT_NAME_TWO, GenericType.CAR, parkingFloor);
        var slotThree = createSlot(SLOT_NAME_THREE, GenericType.BIKE, parkingFloor);
        var slotFour = createSlot(SLOT_NAME_FOUR, GenericType.TRUCK, parkingFloor);
        var vehicleOne =
                createVehicle(
                        KNOWN_REGISTRATION_NUMBER_ONE,
                        GenericType.CAR,
                        parseDate(VEHICLE_ONE_IN_DATE),
                        parseDate(VEHICLE_ONE_OUT_DATE),
                        slotOne);
        var vehicleTwo =
                createVehicle(
                        KNOWN_REGISTRATION_NUMBER_TWO,
                        GenericType.CAR,
                        parseDate(VEHICLE_TWO_IN_DATE),
                        parseDate(VEHICLE_TWO_OUT_DATE),
                        slotTwo);
        var vehicleThree =
                createVehicle(
                        KNOWN_REGISTRATION_NUMBER_THREE,
                        GenericType.BIKE,
                        parseDate(VEHICLE_THREE_IN_DATE),
                        parseDate(VEHICLE_THREE_OUT_DATE),
                        slotThree);
        var vehicleFour =
                createVehicle(
                        KNOWN_REGISTRATION_NUMBER_FOUR,
                        GenericType.TRUCK,
                        parseDate(VEHICLE_FOUR_IN_DATE),
                        parseDate(VEHICLE_FOUR_OUT_DATE),
                        slotFour);

        // persist data
        parkingLotRepository.save(parkingLot);
        jpaParkingFloorRepository.save(parkingFloor);
        jpaSlotRepository.save(slotOne);
        jpaSlotRepository.save(slotTwo);
        jpaSlotRepository.save(slotThree);
        jpaSlotRepository.save(slotFour);
        jpaVehicleRepository.save(vehicleOne);
        jpaVehicleRepository.save(vehicleTwo);
        jpaVehicleRepository.save(vehicleThree);
        jpaVehicleRepository.save(vehicleFour);
    }

    @SneakyThrows
    private Date parseDate(String sDate) {
        return formatter.parse(sDate);
    }

    private String formatDate(Date date) {
        return formatter.format(date);
    }

    private Vehicle createVehicle(
            String registration, GenericType vehicleType, Date inTime, Date outTime, Slot slot) {
        return Vehicle.builder()
                .registrationNumber(registration)
                .vehicleType(vehicleType)
                .inTime(inTime)
                .outTime(outTime)
                .amountInRupees(999)
                .slot(slot)
                .build();
    }

    private Slot createSlot(String slotName, GenericType slotType, ParkingFloor parkingFloor) {
        return Slot.builder()
                .slotType(slotType)
                .slotStatus(SlotStatus.OCCUPIED)
                .parkingFloor(parkingFloor)
                .name(slotName)
                .build();
    }

    @Test
    void findLatestVehicleEntry_ShouldReturnValidEntry() {
        // when
        Optional<Vehicle> latestVehicleEntry =
                jpaVehicleRepository.findLatestVehicleEntry(KNOWN_REGISTRATION_NUMBER_ONE);

        // then
        assertThat(latestVehicleEntry.isEmpty()).isFalse();
    }

    @Test
    void findLatestVehicleEntry_ShouldNotReturnValidEntry() {
        // when
        Optional<Vehicle> latestVehicleEntry =
                jpaVehicleRepository.findLatestVehicleEntry(UNKNOWN_REGISTRATION_NUMBER);

        // then
        assertThat(latestVehicleEntry.isEmpty()).isTrue();
    }

    @Test
    void findLatestVehicleEntry_ShouldReturnValidEntry_MultipleSameRegistration() {
        // given
        var vehicle =
                createVehicle(
                        KNOWN_REGISTRATION_NUMBER_ONE,
                        GenericType.CAR,
                        parseDate(VEHICLE_ONE_IN_DATE_LATER),
                        parseDate(VEHICLE_ONE_OUT_DATE_LATER),
                        slotForReUse);
        jpaVehicleRepository.save(vehicle);

        // when
        Optional<Vehicle> latestVehicleEntry =
                jpaVehicleRepository.findLatestVehicleEntry(KNOWN_REGISTRATION_NUMBER_ONE);

        // then
        assertThat(latestVehicleEntry.isEmpty()).isFalse();
        assertThat(formatDate(latestVehicleEntry.get().getInTime()))
                .isEqualTo(VEHICLE_ONE_IN_DATE_LATER);
    }
}

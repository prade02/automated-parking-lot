package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.NoSlotAvailableException;
import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.models.application.vehicle.Vehicle;
import com.automated.parkinglot.repository.application.SlotRepository;
import com.automated.parkinglot.repository.application.VehicleRepository;
import com.automated.parkinglot.strategies.slot_booking.SlotBookingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class SlotBookingServiceTest {

  private static final String VEHICLE_REGISTRATION = "TN-01-BB-4322";
  private static final String STRATEGY_NAME = "lowestOnAllFromDb";
  private static final String SLOT_NAME = "SLOT_NAME";
  private static final int PARKING_LOT_ID = 1;

  private SlotBookingService slotBookingService;
  @Mock private SlotRepository slotRepository;
  @Mock private VehicleRepository vehicleRepository;
  @Mock private SlotBookingStrategy slotBookingStrategy;
  @Mock private Map<String, SlotBookingStrategy> mapOfStrategies;

  @BeforeEach
  void setUp() {
    given(mapOfStrategies.get(anyString())).willReturn(slotBookingStrategy);
    slotBookingService =
        new SlotBookingService(slotRepository, vehicleRepository, mapOfStrategies, STRATEGY_NAME);
  }

  @Test
  void tryBookingAValidSlot() {
    // given
    var slot = new Slot();
    slot.setName(SLOT_NAME);
    given(slotBookingStrategy.bookSlot(anyInt(), any())).willReturn(slot);

    // when
    ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);
    ArgumentCaptor<Slot> slotArgumentCaptor = ArgumentCaptor.forClass(Slot.class);
    var slotId = slotBookingService.bookSlot(PARKING_LOT_ID, VEHICLE_REGISTRATION, GenericType.CAR);

    // then
    verify(vehicleRepository).save(vehicleArgumentCaptor.capture());
    var capturedVehicle = vehicleArgumentCaptor.getValue();
    assertThat(slotId).isEqualTo(SLOT_NAME);
    assertThat(capturedVehicle.getVehicleType()).isEqualTo(GenericType.CAR);
    assertThat(capturedVehicle.getRegistrationNumber()).isEqualTo(VEHICLE_REGISTRATION);
    assertThat(capturedVehicle.getSlot().getName()).isEqualTo(slot.getName());
  }

  @Test
  void tryBookingASlotWhenNoneAvailable() {
    // given
    given(slotBookingStrategy.bookSlot(anyInt(), any())).willReturn(null);

    // when
    // then
    assertThatThrownBy(
            () ->
                slotBookingService.bookSlot(PARKING_LOT_ID, VEHICLE_REGISTRATION, GenericType.CAR))
        .isInstanceOf(NoSlotAvailableException.class);
  }
}

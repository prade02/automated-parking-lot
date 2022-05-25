package com.automated.parkinglot.service;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.models.vehicle.Vehicle;
import com.automated.parkinglot.repository.SlotRepository;
import com.automated.parkinglot.repository.VehicleRepository;
import com.automated.parkinglot.strategies.slot_booking.SlotBookingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class SlotBookingService implements ISlotBookingService {

    private final SlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final SlotBookingStrategy slotBookingStrategy;

    public SlotBookingService(SlotRepository slotRepository,
                              VehicleRepository vehicleRepository,
                              Map<String, SlotBookingStrategy> slotBookingStrategies,
                              @Value("${slot.booking.strategy}") String slotStrategy) {
        this.slotRepository = slotRepository;
        this.vehicleRepository = vehicleRepository;
        this.slotBookingStrategy = slotBookingStrategies.get(slotStrategy);
    }

    @Override
    public String bookSlot(final int parkingLotId, final String vehicleRegistration, final GenericType slotType) {
        final var slot = slotBookingStrategy.bookSlot(parkingLotId, slotType);
        final Vehicle vehicle = getVehicle(vehicleRegistration, slotType, slot);
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        persistData(vehicle, slot);
        return slot.getName();
    }

    @Transactional
    private void persistData(Vehicle vehicle, Slot slot) {
        vehicleRepository.save(vehicle);
        slotRepository.save(slot);
    }

    private Vehicle getVehicle(String vehicleRegistration, GenericType vehicleType, Slot slot) {
        return Vehicle.builder()
                .vehicleType(vehicleType)
                .registrationNumber(vehicleRegistration)
                .inTime(Date.from(Instant.now()))
                .slot(slot)
                .build();
    }
}

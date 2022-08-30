package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.NoSlotAvailableException;
import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.models.application.vehicle.Vehicle;
import com.automated.parkinglot.repository.application.SlotRepository;
import com.automated.parkinglot.repository.application.VehicleRepository;
import com.automated.parkinglot.strategies.slot_booking.SlotBookingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class SlotBookingService implements ISlotBookingService {

    private final SlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final SlotBookingStrategy slotBookingStrategy;

    public SlotBookingService(
            SlotRepository slotRepository,
            VehicleRepository vehicleRepository,
            Map<String, SlotBookingStrategy> slotBookingStrategies,
            @Value("${slot.booking.strategy}") String slotStrategy) {
        this.slotRepository = slotRepository;
        this.vehicleRepository = vehicleRepository;
        this.slotBookingStrategy = slotBookingStrategies.get(slotStrategy);
    }

    @Override
    @Transactional
    public String bookSlot(
            final int parkingLotId, final String vehicleRegistration, final GenericType slotType) {
        final var slot = slotBookingStrategy.bookSlot(parkingLotId, slotType);
        if (slot == null)
            throw new NoSlotAvailableException("No slot available");
        final Vehicle vehicle = getVehicle(vehicleRegistration, slotType, slot);
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        persistData(vehicle, slot);
        return slot.getName();
    }

    private void persistData(Vehicle vehicle, Slot slot) {
        slotRepository.save(slot);
        vehicleRepository.save(vehicle);
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

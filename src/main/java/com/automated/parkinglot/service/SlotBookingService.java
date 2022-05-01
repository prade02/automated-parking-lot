package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.vehicle.Vehicle;
import com.automated.parkinglot.repository.SlotRepository;
import com.automated.parkinglot.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@AllArgsConstructor
public class SlotBookingService implements ISlotBookingService {

    private final SlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public String bookSlot(int parkingLotId, String vehicleRegistration, GenericType slotType) {
        var slotId = slotRepository.getAvailableSlot(parkingLotId, slotType.name());
        if (slotId == null)
            throw new InvalidRequestException("No slots available");
        var optionalSlot = slotRepository.findById(slotId);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("Unable to allocate slot");
        var slot = optionalSlot.get();
        Vehicle vehicle = Vehicle.builder()
                .vehicleType(slotType)
                .registrationNumber(vehicleRegistration)
                .inTime(Date.from(Instant.now()))
                .slotId(slot.getSlotId())
                .vehicleColor("")
                .build();
        vehicleRepository.save(vehicle);
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);
        return slot.getName();
    }
}

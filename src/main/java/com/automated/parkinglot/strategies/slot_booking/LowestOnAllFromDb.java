package com.automated.parkinglot.strategies.slot_booking;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.repository.SlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LowestOnAllFromDb implements SlotBookingStrategy {

    private final SlotRepository slotRepository;

    @Override
    public Slot bookSlot(int parkingLotId, GenericType slotType) {
        var slotId = slotRepository.getAvailableSlot(parkingLotId, slotType.name());
        if (slotId == null)
            throw new InvalidRequestException("No slots available");
        var optionalSlot = slotRepository.findById(slotId);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("Unable to allocate slot");
        return optionalSlot.get();
    }
}

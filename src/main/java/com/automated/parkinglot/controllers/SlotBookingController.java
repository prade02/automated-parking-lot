package com.automated.parkinglot.controllers;

import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.service.ISlotBookingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/slot-booking")
public class SlotBookingController {

    private final ISlotBookingService slotBookingService;

    @GetMapping("book/{parkingLot}/{vehicleRegistration}/{slotType}")
    public String bookSlot(
            @PathVariable int parkingLot,
            @PathVariable String vehicleRegistration,
            @PathVariable GenericType slotType) {
        // TODO: check if this vehicle has any open entry before booking slot and add relevant unit tests
        return slotBookingService.bookSlot(parkingLot, vehicleRegistration, slotType);
    }
}

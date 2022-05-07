package com.automated.parkinglot.controllers;

import com.automated.parkinglot.service.IBookedSlotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/booked-slot")
public class BookedSlotController {

    private final IBookedSlotService bookedSlotService;

    @GetMapping("release/{registrationNumber}")
    public double releaseSlot(@PathVariable String registrationNumber) {
        return bookedSlotService.releaseSlotAndGetFeeInRupees(registrationNumber);
    }

    @GetMapping("amount/{registrationNumber}")
    public double getAmountTillNow(@PathVariable String registrationNumber) {
        return bookedSlotService.getParkingFee(registrationNumber);
    }
}

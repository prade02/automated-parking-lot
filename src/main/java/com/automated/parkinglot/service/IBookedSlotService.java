package com.automated.parkinglot.service;

public interface IBookedSlotService {
    double releaseSlotAndGetFeeInRupees(String vehicleRegistration);

    double getParkingFee(String vehicleRegistration);
}

package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import com.automated.parkinglot.models.application.parking.Slot;
import com.automated.parkinglot.models.application.vehicle.Vehicle;
import com.automated.parkinglot.repository.application.SlotRepository;
import com.automated.parkinglot.repository.application.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class BookedSlotService implements IBookedSlotService {

    private final SlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public double releaseSlotAndGetFeeInRupees(final String vehicleRegistration) {
        // get the vehicle from db and calculate the fee based on out time.
        final var vehicle = getVehicleInfo(vehicleRegistration);
        vehicleRepository.save(vehicle);

        // update slot as `vacant` in slot table
        releaseSlot(vehicle.getSlot());

        // return the double amount
        return vehicle.getAmountInRupees();
    }

    @Override
    public double getParkingFee(final String registrationNumber) {
        return getVehicleInfo(registrationNumber).getAmountInRupees();
    }

    private Vehicle getVehicleInfo(final String registration) {
        var optionalVehicle = vehicleRepository.findLatestVehicleEntry(registration);
        if (optionalVehicle.isEmpty()) throw new InvalidRequestException("Vehicle info not found");
        final var vehicle = optionalVehicle.get();
        if (vehicle.getOutTime() != null || vehicle.getAmountInRupees() > 0)
            throw new InvalidRequestException("No data found for pending entry");

        // get In and out time, and calculate the hours clocked
        final Date outTime = Date.from(Instant.now());
        final int hoursClocked = getHoursBetweenInAndOut(vehicle.getInTime(), outTime);

        // based on the hours, calculate amount in rupees
        final double amountInRupee = calculateAmountForHours(hoursClocked, vehicle.getVehicleType());

        // persist out and amount in vehicle table
        vehicle.setAmountInRupees(amountInRupee);
        vehicle.setOutTime(outTime);
        return vehicle;
    }

    private void releaseSlot(final Slot slot) {
        slot.setSlotStatus(SlotStatus.VACANT);
        slotRepository.save(slot);
    }

    private int getHoursBetweenInAndOut(final Date inTime, final Date outTime) {
        final long duration = outTime.getTime() - inTime.getTime();
        final long timeInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return (int) Math.ceil((float) timeInMinutes / 60);
    }

    private double calculateAmountForHours(final int hours, final GenericType vehicleType) {
        return hours * vehicleType.getVehicleFeePerHour();
    }
}

package com.automated.parkinglot.service;

import com.automated.parkinglot.exception.InvalidRequestException;
import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import com.automated.parkinglot.models.parking.Slot;
import com.automated.parkinglot.models.vehicle.Vehicle;
import com.automated.parkinglot.repository.SlotRepository;
import com.automated.parkinglot.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SlotBookingService implements ISlotBookingService {

    private final SlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public String bookSlot(final int parkingLotId, final String vehicleRegistration, final GenericType slotType) {
        final var slot = getAvailableSlot(parkingLotId, slotType);
        final Vehicle vehicle = Vehicle.builder()
                .vehicleType(slotType)
                .registrationNumber(vehicleRegistration)
                .inTime(Date.from(Instant.now()))
                .slotId(slot.getSlotId())
                .build();
        vehicleRepository.save(vehicle);
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);
        return slot.getName();
    }

    private Slot getAvailableSlot(int parkingLotId, GenericType slotType) {
        final var slotId = slotRepository.getAvailableSlot(parkingLotId, slotType.name());
        if (slotId == null)
            throw new InvalidRequestException("No slots available");
        final var optionalSlot = slotRepository.findById(slotId);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("Unable to allocate slot");
        return optionalSlot.get();
    }

    @Override
    public double releaseSlotAndGetFeeInRupees(final String vehicleRegistration) {
        // get the vehicle from db and calculate the fee based on out time.
        final var vehicle = getVehicleInfo(vehicleRegistration);
        vehicleRepository.save(vehicle);

        // update slot as `vacant` in slot table
        releaseSlot(vehicle.getSlotId());

        // return the double amount
        return vehicle.getAmountInRupees();
    }

    @Override
    public double getParkingFee(final String registrationNumber) {
        return getVehicleInfo(registrationNumber).getAmountInRupees();
    }

    private Vehicle getVehicleInfo(final String registration) {
        var optionalVehicle = vehicleRepository.findLatestVehicleEntry(registration);
        if (optionalVehicle.isEmpty())
            throw new InvalidRequestException("Vehicle info not found");
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

    private void releaseSlot(final int slotId) {
        final var optionalSlot = slotRepository.findById(slotId);
        if (optionalSlot.isEmpty())
            throw new InvalidRequestException("Slot associated with Vehicle not found");
        final var slot = optionalSlot.get();
        slot.setSlotStatus(SlotStatus.VACANT);
        slotRepository.save(slot);
    }

    private int getHoursBetweenInAndOut(final Date inTime, final Date outTime) {
        final long duration = outTime.getTime() - inTime.getTime();
        final long timeInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return (int) Math.ceil((float) timeInMinutes / 60);
    }

    private double calculateAmountForHours(final int hours, final GenericType vehicleType) {
        return hours * GenericType.getVehicleFeePerHour(vehicleType);
    }
}

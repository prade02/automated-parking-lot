package com.automated.parkinglot.models.enums;

public enum GenericType {
    BIKE,
    CAR,
    TRUCK;

    public static int getVehicleFeePerHour(GenericType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> 10;
            case CAR -> 20;
            case TRUCK -> 30;
        };
    }
}

package com.automated.parkinglot.models.application.enums;

public enum GenericType {
    BIKE,
    CAR,
    TRUCK;

    public int getVehicleFeePerHour() {
        return switch (this) {
            case BIKE -> 10;
            case CAR -> 20;
            case TRUCK -> 30;
        };
    }
}

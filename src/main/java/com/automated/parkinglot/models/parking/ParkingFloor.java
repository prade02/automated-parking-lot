package com.automated.parkinglot.models.parking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @Table(name = "parking_floor")
@Getter @NoArgsConstructor
public class ParkingFloor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingFloorId;

    @Setter
    private String name;
    private int parkingLot;
    private int totalSlots;
}

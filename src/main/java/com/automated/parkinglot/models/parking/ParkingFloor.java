package com.automated.parkinglot.models.parking;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Table(name = "parking_floor")
@Getter @NoArgsConstructor
public class ParkingFloor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingFloorId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private ParkingLot parkingLot;
    private int totalSlots;
}

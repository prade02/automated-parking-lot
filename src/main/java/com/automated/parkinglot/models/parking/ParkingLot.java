package com.automated.parkinglot.models.parking;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity @Table(name = "parking_lot")
@Getter @NoArgsConstructor
public class ParkingLot {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingLotId;

    private String name;
    private int totalFloors;
}

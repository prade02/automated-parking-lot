package com.automated.parkinglot.models.application.parking;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "parking_lot")
@Getter
@NoArgsConstructor
public class ParkingLot {

    @Id
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "parking_lot_id_seq", name = "parking_lot_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_lot_id_seq")
    private int parkingLotId;

    private String name;
    private int totalFloors;
}

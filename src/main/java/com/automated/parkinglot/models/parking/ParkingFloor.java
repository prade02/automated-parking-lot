package com.automated.parkinglot.models.parking;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "parking_floor")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingFloor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingFloorId;

    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "parkingLot", referencedColumnName = "id", nullable = false)
    private ParkingLot parkingLot;

    private int totalSlots;
}

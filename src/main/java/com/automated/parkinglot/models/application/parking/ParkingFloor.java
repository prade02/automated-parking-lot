package com.automated.parkinglot.models.application.parking;

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
    @SequenceGenerator(name = "parking_floor_id_seq", sequenceName = "parking_floor_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_floor_id_seq")
    private int parkingFloorId;

    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "parkingLot", referencedColumnName = "id", nullable = false)
    private ParkingLot parkingLot;

    private int totalSlots;
}

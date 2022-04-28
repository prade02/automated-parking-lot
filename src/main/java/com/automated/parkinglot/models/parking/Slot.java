package com.automated.parkinglot.models.parking;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Slot {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private ParkingFloor parkingFloor;
    private GenericType slotType;

    @Setter
    private SlotStatus slotStatus;
}

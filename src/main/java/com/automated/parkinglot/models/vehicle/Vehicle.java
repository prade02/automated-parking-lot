package com.automated.parkinglot.models.vehicle;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.parking.Slot;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @NoArgsConstructor
public class Vehicle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entryId;

    private String registrationNumber;
    private String vehicleColour;
    private GenericType vehicleType;

    @ManyToOne
    @JoinColumn(name = "slot", referencedColumnName = "id", nullable = true)
    private Slot slotId;

    @Setter
    private Date inTime;
    @Setter
    private Date outTime;
}

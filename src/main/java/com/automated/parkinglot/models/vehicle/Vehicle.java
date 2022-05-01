package com.automated.parkinglot.models.vehicle;

import com.automated.parkinglot.models.enums.GenericType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entryId;

    private String registrationNumber;
    private String vehicleColor;
    @Enumerated(EnumType.STRING)
    private GenericType vehicleType;

    @Column(name = "slot")
    private int slotId;

    @Setter
    private Date inTime;
    @Setter
    private Date outTime;
}

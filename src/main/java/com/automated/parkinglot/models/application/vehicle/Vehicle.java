package com.automated.parkinglot.models.application.vehicle;

import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.parking.Slot;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vehicle")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int entryId;

  private String registrationNumber;

  @Enumerated(EnumType.STRING)
  private GenericType vehicleType;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id", name = "slot", nullable = false)
  private Slot slot;

  @Setter private Date inTime;
  @Setter private Date outTime;

  @Setter
  @Column(name = "fee")
  private double amountInRupees;
}

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int parkingLotId;

  private String name;
  private int totalFloors;
}

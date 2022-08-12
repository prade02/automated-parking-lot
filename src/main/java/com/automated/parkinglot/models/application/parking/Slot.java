package com.automated.parkinglot.models.application.parking;

import com.automated.parkinglot.models.application.enums.GenericType;
import com.automated.parkinglot.models.application.enums.SlotStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int slotId;

  @Setter private String name;

  @ManyToOne
  @JoinColumn(name = "parkingFloor", referencedColumnName = "id", nullable = false)
  private ParkingFloor parkingFloor;

  @Enumerated(EnumType.STRING)
  private GenericType slotType;

  @Setter
  @Enumerated(EnumType.STRING)
  private SlotStatus slotStatus;
}

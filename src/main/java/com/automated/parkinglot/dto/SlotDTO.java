package com.automated.parkinglot.dto;

import com.automated.parkinglot.models.enums.GenericType;
import com.automated.parkinglot.models.enums.SlotStatus;
import lombok.Getter;

@Getter
public class SlotDTO {

    private int slotId;
    private String name;
    private GenericType slotType;
    private SlotStatus slotStatus;
}

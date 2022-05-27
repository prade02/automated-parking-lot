package com.automated.parkinglot.exception;

public class NoSlotAvailableException extends RuntimeException {
  public NoSlotAvailableException(final String message) {
    super(message);
  }
}

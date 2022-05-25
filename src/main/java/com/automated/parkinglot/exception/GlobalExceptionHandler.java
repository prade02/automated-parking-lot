package com.automated.parkinglot.exception;

import com.automated.parkinglot.exception.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleInternalError(Exception exception) {
    return new ErrorResponse(String.format("Internal error: %s", exception.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {InvalidRequestException.class})
  public ErrorResponse handleInvalidRequest(Exception exception) {
    return new ErrorResponse(String.format("Error: %s", exception.getMessage()));
  }
}

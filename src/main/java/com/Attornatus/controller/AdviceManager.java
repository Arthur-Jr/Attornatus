package com.Attornatus.controller;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Attornatus.exception.DataError;
import com.Attornatus.exception.ErrorMessages;

@ControllerAdvice
public class AdviceManager {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<DataError> handleInvalidPayload(MethodArgumentNotValidException e) {
    DataError errorResponse = new DataError(e.getFieldError().getDefaultMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<DataError> handleinvalidDate(DateTimeParseException e) {
    DataError errorResponse = new DataError(ErrorMessages.invalidDateFormat);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}

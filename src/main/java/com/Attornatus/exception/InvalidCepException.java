package com.Attornatus.exception;

public class InvalidCepException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidCepException() {
    super(ErrorMessages.invalidCep);
  }
}

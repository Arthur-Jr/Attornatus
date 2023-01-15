package com.Attornatus.exception;

public class PersonNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PersonNotFoundException() {
    super(ErrorMessages.PersonNotFound);
  }
}

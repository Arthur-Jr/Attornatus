package com.Attornatus.exception;

public class AddressNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public AddressNotFoundException() {
    super(ErrorMessages.AddressNotFound);
  }
}

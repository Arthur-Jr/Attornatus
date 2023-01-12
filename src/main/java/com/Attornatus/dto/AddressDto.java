package com.Attornatus.dto;

import com.Attornatus.exception.ErrorMessages;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AddressDto {
  @NotEmpty(message = ErrorMessages.emptyNumber)
  private int number;

  private boolean principal = false;

  @NotEmpty(message = ErrorMessages.emptyCity)
  private String city;

  @NotEmpty(message = ErrorMessages.emptyStreet)
  private String street;

  @NotEmpty(message = ErrorMessages.emptyCep)
  @Size(max = 8, min = 8, message = ErrorMessages.cepSize)
  private String cep;

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public boolean isPrincipal() {
    return principal;
  }

  public void setPrincipal(boolean principal) {
    this.principal = principal;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }
}

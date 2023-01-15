package com.Attornatus.exception;

public class ErrorMessages {
  public static final String emptyName = "Person name is required!";
  public static final String nameSize = "name must have at least 2 characters!";
  public static final String emptyBirthDate = "Person birthdate is required!";
  public static final String invalidDateFormat = "Invalide date format! Date format ex: 01/01/2000";

  public static final String emptyNumber = "Address number is required!";
  public static final String emptyCity = "Address city is required!";
  public static final String emptyStreet = "Address street name is required!";
  public static final String emptyCep = "Address CEP is required!";
  public static final String cepSize = "Address CEP must have 8 characters!";
  public static final String invalidCep = "Invalid CEP! CEP ex: 12345678";

  public static final String addressAlreadyRegistred = "There already an address with this number in this street!";

  public static final String PersonNotFound = "Person not found!";
}

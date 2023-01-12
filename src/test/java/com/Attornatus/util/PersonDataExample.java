package com.Attornatus.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonDataExample {
  private static String name = "Test";
  private static String birthDate = "01/05/1996";
  private static LocalDate birthDateFormatted = LocalDate
    .parse(birthDate, DateTimeFormatter.ofPattern("dd/MM/yyy"));

  public static String getName() {
    return name;
  }

  public static String getBirthDate() {
    return birthDate;
  }

  public static LocalDate getBirthDateFormatted() {
    return birthDateFormatted;
  } 
}

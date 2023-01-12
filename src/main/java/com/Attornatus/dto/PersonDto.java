package com.Attornatus.dto;

import com.Attornatus.exception.ErrorMessages;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDto {
  @NotEmpty(message = ErrorMessages.emptyName)
  @Size(min = 3, message = ErrorMessages.nameSize)
  private String name;

  @NotEmpty(message = ErrorMessages.emptyBirthDate)
  private String BirthDate;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBirthDate() {
    return BirthDate;
  }

  public void setBirthDate(String birthDate) {
    BirthDate = birthDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((BirthDate == null) ? 0 : BirthDate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PersonDto other = (PersonDto) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (BirthDate == null) {
      if (other.BirthDate != null)
        return false;
    } else if (!BirthDate.equals(other.BirthDate))
      return false;
    return true;
  }
}

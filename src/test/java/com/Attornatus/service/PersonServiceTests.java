package com.Attornatus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Attornatus.dto.PersonDto;
import com.Attornatus.dto.PersonEditDto;
import com.Attornatus.model.Person;
import com.Attornatus.repository.PersonRepository;
import com.Attornatus.util.PersonDataExample;

@ExtendWith(MockitoExtension.class)
@DisplayName("Person Service:")
public class PersonServiceTests {
  @InjectMocks
  private PersonService service;
  
  @Mock
  private PersonRepository repo;

  private PersonDto personDto = new PersonDto();
  private PersonEditDto personEditDto = new PersonEditDto();
  private Person person = new Person();

  @BeforeEach
  public void initEach() {
    this.personDto.setName(PersonDataExample.getName());
    this.personDto.setBirthDate(PersonDataExample.getBirthDate());

    this.personEditDto.setBirthDate(null);
    this.personEditDto.setName(null);

    this.person.setName(PersonDataExample.getName());
    this.person.setBirthDate(PersonDataExample.getBirthDateFormatted());
    this.person.setId(Long.valueOf(1));
  }
  
  @Test
  @DisplayName("Register Person test: get personDto and create a Person and return it")
  void should_save_one_Person() {
    when(this.repo.save(any(Person.class))).thenReturn(this.person);
    Person createdPerson = this.service.registerPerson(this.personDto);

    assertEquals(this.personDto.getName(), createdPerson.getName());
    assertEquals(PersonDataExample.getBirthDateFormatted(), createdPerson.getBirthDate());
    verify(repo, times(1)).save(any(Person.class));
  }

  @Test
  @DisplayName("Register Person test: should throw if date format is invalid")
  void should_throw_datetimeException_if_date_format_invalid() {
    this.personDto.setBirthDate("2000/01/01");

    Throwable exception = assertThrows(DateTimeParseException.class, () -> {
      this.service.registerPerson(this.personDto);
    });

    assertNotNull(exception);
    assertEquals("Text '2000/01/01' could not be parsed at index 2", exception.getMessage());
    verify(repo, times(0)).save(any(Person.class));
  }

  @Test
  @DisplayName("List Person test: Should return a empty list")
  void should_return_a_empty_person_list() {
    when(this.repo.findAll()).thenReturn(new ArrayList<Person>());

    int personListSize = this.service.listPerson().size();

    assertEquals(0, personListSize);
    verify(repo, times(1)).findAll();
  }

  @Test
  @DisplayName("Edit Person test: name edit test")
  void name_edit_test() {
    this.personEditDto.setName("Test_2");
    when(this.repo.getReferenceById(Long.valueOf(1))).thenReturn(this.person);

    person.setName(this.personDto.getName());
    when(this.repo.save(person)).thenReturn(person);
    Person createdPerson = this.service.editPerson(this.personEditDto, Long.valueOf(1));

    assertEquals(this.personEditDto.getName(), createdPerson.getName());
    assertEquals(PersonDataExample.getBirthDateFormatted(), createdPerson.getBirthDate());
    assertEquals(this.person.getId(), createdPerson.getId());
    verify(repo, times(1)).save(person);
    verify(repo, times(1)).getReferenceById(Long.valueOf(1));
  }

  @Test
  @DisplayName("Edit Person test: birthdate edit test")
  void birthdate_edit_test() {
    this.personEditDto.setBirthDate("12/12/2010");
    when(this.repo.getReferenceById(Long.valueOf(1))).thenReturn(this.person);

    LocalDate dateToEdit = LocalDate
      .parse(this.personEditDto.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyy"));

    this.person.setBirthDate(dateToEdit);
    when(this.repo.save(this.person)).thenReturn(this.person);
    Person createdPerson = this.service.editPerson(this.personEditDto, Long.valueOf(1));

    assertEquals(PersonDataExample.getName(), createdPerson.getName());
    assertEquals(dateToEdit, createdPerson.getBirthDate());
    assertEquals(this.person.getId(), createdPerson.getId());
    verify(repo, times(1)).save(person);
    verify(repo, times(1)).getReferenceById(Long.valueOf(1));
  }

  @Test
  @DisplayName("Edit Person test: should throw if date format is invalid")
  void should_thow_if_birthdate_format_is_invalid() {
    this.personEditDto.setBirthDate("2010/12/12");

    Throwable exception = assertThrows(DateTimeParseException.class, () -> {
      this.service.editPerson(this.personEditDto, Long.valueOf(1));
    });

    assertNotNull(exception);
    assertEquals("Text '2010/12/12' could not be parsed at index 2", exception.getMessage());
    verify(repo, times(0)).save(any(Person.class));
    verify(repo, times(1)).getReferenceById(Long.valueOf(1));
  }
}

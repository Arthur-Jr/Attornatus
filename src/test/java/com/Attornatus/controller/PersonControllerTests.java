package com.Attornatus.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.Attornatus.dto.PersonDto;
import com.Attornatus.dto.PersonEditDto;
import com.Attornatus.exception.ErrorMessages;
import com.Attornatus.model.Person;
import com.Attornatus.service.PersonService;
import com.Attornatus.util.PersonDataExample;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@WebMvcTest(PersonController.class)
@DisplayName("Person controller:")
public class PersonControllerTests {
  @Autowired
  private MockMvc mock;

  @MockBean
  private PersonService service;

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
  @DisplayName("Register person test: should have status code 201 and return person data on body")
  void should_have_statusCode201_when_person_created() throws Exception {
    when(this.service.registerPerson(this.personDto)).thenReturn(this.person);
    ResultActions response = this.registerPerson(this.personDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.name").value("Test"))
      .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when name is empty")
  void should_return_error_message_when_name_is_empty() throws Exception {
    this.personDto.setName(null);
    ResultActions response = this.registerPerson(this.personDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.emptyName));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when name size ie less than three")
  void should_return_error_message_when_name_size_is_less_than_three() throws Exception {
    this.personDto.setName("ab");
    ResultActions response = this.registerPerson(this.personDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.nameSize));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when birthdate is empty")
  void should_return_error_message_when_birthdate_is_empty() throws Exception {
    this.personDto.setBirthDate(null);
    ResultActions response = this.registerPerson(this.personDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.emptyBirthDate));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when birthdate has invalid format")
  void should_return_statuscode_400_if_date_format_is_invalid() throws Exception {
    this.personDto.setBirthDate("2000/01/01");

    when(this.service.registerPerson(this.personDto)).thenThrow(DateTimeParseException.class);
    ResultActions response = this.registerPerson(this.personDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.invalidDateFormat));
  }

  @Test
  @DisplayName("List person test: Should return a empty list with status code 200")
  void should_return_a_empty_list_with_statuscode_200() throws Exception {
    when(this.service.listPerson()).thenReturn(new ArrayList<Person>());

    this.mock.perform(get("/person"))
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.size()").value(0));;
  }

  @Test
  @DisplayName("Edit person test: name edit test")
  void name_edit_test() throws Exception {
    this.personEditDto.setName("Test_2");

    this.person.setName(this.personEditDto.getName());
    when(this.service.editPerson(this.personEditDto, Long.valueOf(1))).thenReturn(person);
    ResultActions response = this.editPerson(this.personEditDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Test_2"))
      .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  @DisplayName("Edit person test: should have status code 400 when name size ie less than three")
  void name_edit_validation_test() throws Exception {
    this.personEditDto.setName("ab");
    ResultActions response = this.editPerson(this.personEditDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.nameSize));
  }

  @Test
  @DisplayName("Edit person test: should have status code 400 when birthdate has invalid format")
  void birthdate_edit_validation_test() throws Exception {
    this.personEditDto.setBirthDate("2000/01/01");

    when(this.service.editPerson(this.personEditDto, Long.valueOf(1)))
      .thenThrow(DateTimeParseException.class);

    ResultActions response = this.editPerson(this.personEditDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.invalidDateFormat));
  }

  /*
    Register person endpoint method.
  */
  private ResultActions registerPerson(PersonDto payload) throws Exception {
    return this.mock.perform(post("/person")
    .contentType(MediaType.APPLICATION_JSON)
    .content(new ObjectMapper().writeValueAsString(payload)));
  }

  /*
    Edit person endpoint method.
  */
  private ResultActions editPerson(PersonEditDto payload) throws Exception {
    return this.mock.perform(put("/person/1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(new ObjectMapper().writeValueAsString(payload)));
  }
}

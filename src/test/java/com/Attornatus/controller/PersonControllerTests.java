package com.Attornatus.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.Attornatus.dto.PersonDto;
import com.Attornatus.exception.ErrorMessages;
import com.Attornatus.model.Person;
import com.Attornatus.service.PersonService;
import com.Attornatus.util.PersonDataExample;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.format.DateTimeParseException;

@WebMvcTest(PersonController.class)
@DisplayName("Person controller:")
public class PersonControllerTests {
  @Autowired
  private MockMvc mock;

  @MockBean
  private PersonService service;

  @Test
  @DisplayName("Register person test: should have status code 201 and return person data on body")
  void should_have_statusCode201_when_person_created() throws Exception {
    PersonDto payload = new PersonDto();
    payload.setName(PersonDataExample.getName());
    payload.setBirthDate(PersonDataExample.getBirthDate());

    Person person = new Person();
    person.setName(payload.getName());
    person.setBirthDate(PersonDataExample.getBirthDateFormatted());
    when(this.service.registerPerson(payload)).thenReturn(person);

    ResultActions response = this.registerPerson(payload);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.name").value("Test"));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when name is empty")
  void should_return_error_message_when_name_is_empty() throws Exception {
    PersonDto payload = new PersonDto();
    payload.setBirthDate(PersonDataExample.getBirthDate());

    ResultActions response = this.registerPerson(payload);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.emptyName));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when name size ie less than three")
  void should_return_error_message_when_name_size_is_less_than_three() throws Exception {
    PersonDto payload = new PersonDto();
    payload.setName("ab");
    payload.setBirthDate(PersonDataExample.getBirthDate());

    ResultActions response = this.registerPerson(payload);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.nameSize));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when birthdate is empty")
  void should_return_error_message_when_birthdate_is_empty() throws Exception {
    PersonDto payload = new PersonDto();
    payload.setName(PersonDataExample.getName());

    ResultActions response = this.registerPerson(payload);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(ErrorMessages.emptyBirthDate));
  }

  @Test
  @DisplayName("Register person test: should have status code 400 when birthdate has invalid format")
  void should_return_statuscode_400_if_date_format_is_invalid() throws Exception {
    PersonDto payload = new PersonDto();
    payload.setName(PersonDataExample.getName());
    payload.setBirthDate(PersonDataExample.getBirthDate());

    when(this.service.registerPerson(payload)).thenThrow(DateTimeParseException.class);
    ResultActions response = this.registerPerson(payload);

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
}

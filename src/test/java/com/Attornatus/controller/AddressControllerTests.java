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

import com.Attornatus.dto.AddressDto;
import com.Attornatus.exception.AddressNotFoundException;
import com.Attornatus.exception.ErrorMessages;
import com.Attornatus.model.Address;
import com.Attornatus.service.AddressService;
import com.Attornatus.util.AddressDataExample;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AddressController.class)
@DisplayName("Address controller:")
public class AddressControllerTests {
  @Autowired
  private MockMvc mock;

  @MockBean
  private AddressService service;

  private AddressDto addressDto = new AddressDto();
  private Address address = new Address();

  @BeforeEach
  public void initEach() {
    this.addressDto.setCep(AddressDataExample.getCep());
    this.addressDto.setCity(AddressDataExample.getCityName());
    this.addressDto.setNumber(AddressDataExample.getNumber());
    this.addressDto.setStreet(AddressDataExample.getStreetName());
    this.addressDto.setPrincipal(AddressDataExample.isPrincipal());

    this.address.setNumber(AddressDataExample.getNumber());
    this.address.setPrincipal(AddressDataExample.isPrincipal());
    this.address.setId(Long.valueOf(1));
  }

  @Test
  @DisplayName("Add Address tests: should have status code 201 and return address data on body")
  void should_have_statusCode201_when_address_is_added() throws Exception {
    when(this.service.addNewAddress(this.addressDto, Long.valueOf(1))).thenReturn(this.address);
    ResultActions addedAddress = this.addNewAddress(this.addressDto);

    addedAddress.andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.number").value(1));
  }

  @Test
  @DisplayName("Add Address tests: should have status code 400 when CEP is empty")
  void should_return_error_message_when_cep_is_empty() throws Exception {
    this.addressDto.setCep(null);
    ResultActions response = this.addNewAddress(this.addressDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.message").value(ErrorMessages.emptyCep));
  }

  @Test
  @DisplayName("Add Address tests: should have status code 400 when city is empty")
  void should_return_error_message_when_city_is_empty() throws Exception {
    this.addressDto.setCity(null);
    ResultActions response = this.addNewAddress(this.addressDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.message").value(ErrorMessages.emptyCity));
  }

  @Test
  @DisplayName("Add Address tests: should have status code 400 when street is empty")
  void should_return_error_message_when_street_is_empty() throws Exception {
    this.addressDto.setStreet(null);
    ResultActions response = this.addNewAddress(this.addressDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.message").value(ErrorMessages.emptyStreet));
  }

  @Test
  @DisplayName("Add Address tests: should have status code 400 when number is empty")
  void should_return_error_message_when_number_is_empty() throws Exception {
    this.addressDto.setNumber(null);
    ResultActions response = this.addNewAddress(this.addressDto);

    response.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.message").value(ErrorMessages.emptyNumber));
  }

  @Test
  @DisplayName("Find Address by person tests: should return Address list from person")
  void should_return_address_list_of_the_person() throws Exception {
    List<Address> addressList = new ArrayList<>();
    addressList.add(this.address);
    when(this.service.findAllAddressByPersonId(Long.valueOf(1))).thenReturn(addressList);

    this.mock.perform(get("/address/person/1"))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(1));
  }

  @Test
  @DisplayName("Find by id: should throw not found if the address is not on DB")
  void should_throw_if_address_is_not_on_db() throws Exception {
    when(this.service.findAddresById(Long.valueOf(1)))
      .thenThrow(AddressNotFoundException.class);
    
    this.mock.perform(get("/address/1"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value(ErrorMessages.AddressNotFound));
  }

  @Test
  @DisplayName("Find by id: should return the Address")
  void should_return_the_selected_address() throws Exception {
    when(this.service.findAddresById(Long.valueOf(1)))
      .thenReturn(this.address);
    
    this.mock.perform(get("/address/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.number").value(this.address.getNumber()));
  }

  private ResultActions addNewAddress(AddressDto payload) throws Exception {
    return this.mock.perform(post("/address/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(new ObjectMapper().writeValueAsString(payload)));  
  }
}

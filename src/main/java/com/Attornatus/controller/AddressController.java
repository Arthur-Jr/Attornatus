package com.Attornatus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Attornatus.dto.AddressDto;
import com.Attornatus.model.Address;
import com.Attornatus.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/address")
public class AddressController {
  @Autowired
  private AddressService service;

  @PostMapping("/{id}")
  public ResponseEntity<Address> addNewAddress(@Valid @RequestBody AddressDto address, @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .contentType(MediaType.APPLICATION_JSON)
      .body(this.service.addNewAddress(address, id));
  }
}

package com.Attornatus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/person/{personId}")
  public List<Address> findAllAddressByPersonId(@PathVariable Long personId) {
    return this.service.findAllAddressByPersonId(personId);
  }

  @GetMapping("/{addressId}")
  public ResponseEntity<Address> findAddresById(@PathVariable Long addressId) {
    return ResponseEntity.status(HttpStatus.OK)
      .body(this.service.findAddresById(addressId));
  }

  @PutMapping("/{addressId}")
  public ResponseEntity<Address> changePrincipalAddress(@PathVariable Long addressId) {
    return ResponseEntity.ok(this.service.changePrincipalAddress(addressId));
  }
}

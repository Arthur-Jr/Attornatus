package com.Attornatus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Attornatus.dto.PersonDto;
import com.Attornatus.model.Person;
import com.Attornatus.service.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/person")
public class PersonController {
  @Autowired
  private PersonService service;
  
  @PostMapping
  public ResponseEntity<Person> registerPerson(@Valid @RequestBody PersonDto person) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .contentType(MediaType.APPLICATION_JSON)
      .body(this.service.registerPerson(person));
  }

  @GetMapping
  public List<Person> listPerson() {
    return this.service.listPerson();
  }
}

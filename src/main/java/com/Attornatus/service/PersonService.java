package com.Attornatus.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Attornatus.dto.PersonDto;
import com.Attornatus.model.Person;
import com.Attornatus.repository.PersonRepository;

@Service
public class PersonService {
  @Autowired
  private PersonRepository repo;

  public Person registerPerson(PersonDto payload) {
    Person person = new Person();
    person.setName(payload.getName());
    person.setBirthDate(LocalDate.parse(payload.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));

    return this.repo.save(person);
  }
}

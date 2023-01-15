package com.Attornatus.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Attornatus.dto.PersonDto;
import com.Attornatus.dto.PersonEditDto;
import com.Attornatus.exception.PersonNotFoundException;
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

  public List<Person> listPerson() {
    return this.repo.findAll();
  }

  public Person editPerson(PersonEditDto payload, Long id) {
    Person personToEdit = this.repo.getReferenceById(id);
    if (payload.getName() != null) {
      personToEdit.setName(payload.getName());
    }

    if (payload.getBirthDate() != null) {
      personToEdit
        .setBirthDate(LocalDate.parse(payload.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    return this.repo.save(personToEdit);
  }

  public Person findPersonById(Long id) {
    Person person = this.repo.findById(id).orElse(null);

    if (person == null) {
      throw new PersonNotFoundException();
    }

    return person;
  }
}

package com.Attornatus.Jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.Attornatus.model.Person;
import com.Attornatus.repository.PersonRepository;
import com.Attornatus.util.PersonDataExample;

@DataJpaTest
@DisplayName("Person JPA:")
public class PersonRepositoryTests {
  @Autowired
  private PersonRepository repo;

  @Test
  @DisplayName("Register person test: should save a person and return it")
  void should_save_one_Person() {
    Person person = new Person();
    person.setName(PersonDataExample.getName());
    person.setBirthDate(PersonDataExample.getBirthDateFormatted());
    Person addedPerson = this.repo.save(person);
    Integer personCount = this.repo.findAll().size();

    assertNotNull(addedPerson);
    assertEquals(1, addedPerson.getId());
    assertEquals(1, personCount);
  }
}

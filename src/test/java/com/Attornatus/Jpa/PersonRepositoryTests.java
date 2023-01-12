package com.Attornatus.Jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
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

  private Person person = new Person();

  @BeforeEach
  public void initEach() {
    this.person.setName(PersonDataExample.getName());
    this.person.setBirthDate(PersonDataExample.getBirthDateFormatted());

    this.repo.save(this.person);
  }

  @Test
  @DisplayName("Register person test: should save a person and return it")
  void should_save_one_Person() {
    Person personToAdd = new Person();
    personToAdd.setName("Test_2");
    personToAdd.setBirthDate(PersonDataExample.getBirthDateFormatted());

    Person addedPerson = this.repo.save(personToAdd);
    Integer personCount = this.repo.findAll().size();

    assertNotNull(addedPerson);
    assertEquals(personToAdd.getName(), addedPerson.getName());
    assertEquals(2, personCount);
  }

  @Test
  @DisplayName("Edit person test: should return person with edited name")
  void should_return_person_with_edited_name() {
    Person personToEdit = this.repo.getReferenceById(Long.valueOf(1));
    personToEdit.setName("Test_1");

    this.repo.save(personToEdit);
    Person editedPerson = this.repo.getReferenceById(Long.valueOf(1));

    assertNotNull(editedPerson);
    assertEquals(personToEdit.getId(), editedPerson.getId());
    assertEquals("Test_1", editedPerson.getName());
    assertEquals(PersonDataExample.getBirthDateFormatted(), editedPerson.getBirthDate());
  }
}

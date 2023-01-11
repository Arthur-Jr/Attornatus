package com.Attornatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attornatus.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
  
}

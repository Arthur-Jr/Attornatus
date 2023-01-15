package com.Attornatus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attornatus.model.Address;
import com.Attornatus.model.Person;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findAllByPerson(Person person);
}

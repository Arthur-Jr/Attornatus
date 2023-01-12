package com.Attornatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attornatus.model.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {
  Street findByCep(String cep);

  Street findByStreetName(String streetName);
}

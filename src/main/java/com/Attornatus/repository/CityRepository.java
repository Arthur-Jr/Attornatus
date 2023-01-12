package com.Attornatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Attornatus.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
  
}

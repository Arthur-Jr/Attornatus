package com.Attornatus.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "city_name")
  private String cityName;

  @OneToMany(mappedBy = "city")
  private List<Street> streets;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public List<Street> getStreets() {
    return streets;
  }

  public void setStreets(List<Street> streets) {
    this.streets = streets;
  }
}

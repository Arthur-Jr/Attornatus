package com.Attornatus.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Street {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "street_name")
  private String streetName;

  private String cep;

  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private City city;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }
}

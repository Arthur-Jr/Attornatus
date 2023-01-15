package com.Attornatus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Attornatus.dto.AddressDto;
import com.Attornatus.exception.AddressNotFoundException;
import com.Attornatus.exception.InvalidCepException;
import com.Attornatus.model.Address;
import com.Attornatus.model.City;
import com.Attornatus.model.Person;
import com.Attornatus.model.Street;
import com.Attornatus.repository.AddressRepository;
import com.Attornatus.repository.CityRepository;
import com.Attornatus.repository.StreetRepository;

@Service
public class AddressService {
  @Autowired
  private AddressRepository addressRepo;

  @Autowired
  private PersonService personService;

  @Autowired
  private CityRepository cityRepo;

  @Autowired
  private StreetRepository streetRepo;

  public Address addNewAddress(AddressDto payload, Long personId) {
    Person person = this.personService.findPersonById(personId);
    List<Address> personAddressList = this.addressRepo.findAllByPerson(person);

    City city = this.checkIfCityAlreadyExists(payload.getCity().toLowerCase());
    Street street = this.checkIfStreetAlreadyExists(city, payload.getStreet(), payload.getCep());

    if (personAddressList.size() > 0 && payload.isPrincipal()) {
      this.changePrincipalAddress(personAddressList);
    }

    Address address = new Address();
    address.setNumber(payload.getNumber());
    address.setPerson(person);
    address.setPrincipal(payload.isPrincipal());
    address.setStreet(street);
    if (personAddressList.size() == 0) { address.setPrincipal(true); }

    return this.addressRepo.save(address);
  }

  public List<Address> findAllAddressByPersonId(Long personId) {
    Person person = this.personService.findPersonById(personId);
    return this.addressRepo.findAllByPerson(person);
  }

  public Address findAddresById(Long addressId) {
    Address address = this.addressRepo.getReferenceById(addressId);

    if (address == null) {
      throw new AddressNotFoundException();
    }

    return address;
  }

  public Address changePrincipalAddress(Long addressId) {
    Address address = this.findAddresById(addressId);
    List<Address> addressList = this.addressRepo.findAllByPerson(address.getPerson());
    this.changePrincipalAddress(addressList);

    address.setPrincipal(true);
    return this.addressRepo.save(address);
  }

  private void changePrincipalAddress(List<Address> addressList) {
    Address addressToChange = addressList.stream()
      .filter(x -> x.isPrincipal() == true)
      .findFirst().orElse(null);

    addressToChange.setPrincipal(false);
    this.addressRepo.save(addressToChange);
  }

  private City checkIfCityAlreadyExists(String cityName) {
    City city = this.cityRepo.findByCityName(cityName);

    if (city == null) {
      city = new City();
      city.setCityName(cityName);
      return this.cityRepo.save(city);
    }

    return city;
  }

  private Street checkIfStreetAlreadyExists(City city, String streetName, String cep) {
    if (!cep.matches("[0-9]+")) {
      throw new InvalidCepException();
    }

    Street street = this.streetRepo.findByStreetName(streetName);
    street = this.streetRepo.findByCep(cep); // Checa se o cep existe com um "streetName" diferente.

    if (street == null) {
      street = new Street();
      street.setCity(city);
      street.setStreetName(streetName);
      street.setCep(cep);

      return this.streetRepo.save(street);
    }

    return street;
  }
}

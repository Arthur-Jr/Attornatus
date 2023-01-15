package com.Attornatus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Attornatus.dto.AddressDto;
import com.Attornatus.exception.AddressNotFoundException;
import com.Attornatus.exception.ErrorMessages;
import com.Attornatus.exception.InvalidCepException;
import com.Attornatus.model.Address;
import com.Attornatus.model.City;
import com.Attornatus.model.Person;
import com.Attornatus.model.Street;
import com.Attornatus.repository.AddressRepository;
import com.Attornatus.repository.CityRepository;
import com.Attornatus.repository.StreetRepository;
import com.Attornatus.util.AddressDataExample;
import com.Attornatus.util.PersonDataExample;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address service:")
public class AddressSerivceTest {
  @InjectMocks
  private AddressService service;

  @Mock
  private AddressRepository addressRepo;

  @Mock
  private CityRepository cityRepo;

  @Mock
  private StreetRepository streetRepo;

  @Mock
  private PersonService personService;

  private AddressDto addressDto = new AddressDto();
  private Address address = new Address();
  private Person person = new Person();
  private City city = new City();
  private Street street = new Street();

  @BeforeEach
  public void initEach() {
    this.addressDto.setCep(AddressDataExample.getCep());
    this.addressDto.setCity(AddressDataExample.getCityName());
    this.addressDto.setNumber(AddressDataExample.getNumber());
    this.addressDto.setStreet(AddressDataExample.getStreetName());
    this.addressDto.setPrincipal(AddressDataExample.isPrincipal());

    this.person.setName(PersonDataExample.getName());
    this.person.setBirthDate(PersonDataExample.getBirthDateFormatted());
    this.person.setId(Long.valueOf(1));

    this.city.setCityName(AddressDataExample.getCityName());
    this.city.setId(Long.valueOf(1));

    this.street.setCep(AddressDataExample.getCep());
    this.street.setStreetName(AddressDataExample.getStreetName());
    this.street.setId(Long.valueOf(1));
    this.street.setCity(city);

    this.address.setNumber(AddressDataExample.getNumber());
    this.address.setPrincipal(AddressDataExample.isPrincipal());
    this.address.setId(Long.valueOf(1));
    this.address.setPerson(person);
    this.address.setStreet(street);
  }

  @Test
  @DisplayName("Add address test: should save new address, new city and street")
  void should_save_a_new_address_city_and_street() throws Exception {
    when(this.personService.findPersonById(Long.valueOf(1))).thenReturn(this.person);
    when(this.addressRepo.findAllByPerson(person)).thenReturn(new ArrayList<Address>());
    this.findCityStreet(null, null);
    this.saveAddressMock(this.address);
    this.saveCityMock();
    this.saveStreetMock();
    Address addressAdded = this.service.addNewAddress(this.addressDto, Long.valueOf(1));

    verify(this.addressRepo, times(1)).findAllByPerson(this.person);
    verify(this.cityRepo, times(1)).findByCityName(anyString());
    verify(this.cityRepo, times(1)).save(any(City.class));
    verify(this.streetRepo, times(1)).findByStreetName(anyString());
    verify(this.streetRepo, times(1)).findByCep(anyString());
    verify(this.streetRepo, times(1)).save(any(Street.class));
    verify(this.addressRepo, times(1)).save(any(Address.class));
    assertEquals(this.address, addressAdded);
  }

  @Test
  @DisplayName("Add address test: should save a new address using a existing city")
  void should_save_a_new_address_and_street_and_use_a_city_that_already_exists() throws Exception {
    when(this.personService.findPersonById(Long.valueOf(1))).thenReturn(this.person);
    when(this.addressRepo.findAllByPerson(person)).thenReturn(new ArrayList<Address>());
    this.findCityStreet(this.city, null);
    this.saveAddressMock(this.address);
    this.saveStreetMock();
    Address addressAdded = this.service.addNewAddress(this.addressDto, Long.valueOf(1));

    verify(this.addressRepo, times(1)).findAllByPerson(this.person);
    verify(this.cityRepo, times(1)).findByCityName(anyString());
    verify(this.cityRepo, times(0)).save(any(City.class));
    verify(this.streetRepo, times(1)).findByStreetName(anyString());
    verify(this.streetRepo, times(1)).findByCep(anyString());
    verify(this.streetRepo, times(1)).save(any(Street.class));
    assertEquals(this.address, addressAdded);
  }

  @Test
  @DisplayName("Add address test: should save a new address using a existing city")
  void should_save_a_new_address_and_city_and_use_a_street_that_already_exists() throws Exception {
    when(this.personService.findPersonById(Long.valueOf(1))).thenReturn(this.person);
    when(this.addressRepo.findAllByPerson(person)).thenReturn(new ArrayList<Address>());
    this.findCityStreet(null, this.street);
    this.saveAddressMock(this.address);
    this.saveCityMock();
    Address addressAdded = this.service.addNewAddress(this.addressDto, Long.valueOf(1));

    verify(this.addressRepo, times(1)).findAllByPerson(this.person);
    verify(this.cityRepo, times(1)).findByCityName(anyString());
    verify(this.cityRepo, times(1)).save(any(City.class));
    verify(this.streetRepo, times(1)).findByStreetName(anyString());
    verify(this.streetRepo, times(1)).findByCep(anyString());
    verify(this.streetRepo, times(0)).save(any(Street.class));
    assertEquals(this.address, addressAdded);
  }

  @Test
  @DisplayName("Add address test: should throw an exception when CEP is invalid")
  void should_throw_when_cep_is_invalid() throws Exception {
    when(this.personService.findPersonById(Long.valueOf(1))).thenReturn(this.person);
    when(this.addressRepo.findAllByPerson(person)).thenReturn(new ArrayList<Address>());
    when(this.cityRepo.findByCityName(anyString())).thenReturn(this.city);
    this.addressDto.setCep("abcdefgh");

    Throwable exception = assertThrows(InvalidCepException.class, () -> {
      this.service.addNewAddress(this.addressDto, Long.valueOf(1));
    });

    assertNotNull(exception);
    assertEquals(ErrorMessages.invalidCep, exception.getMessage());
    verify(this.addressRepo, times(1)).findAllByPerson(this.person);
    verify(this.cityRepo, times(1)).findByCityName(anyString());
    verify(this.cityRepo, times(0)).save(any(City.class));
    verify(this.streetRepo, times(0)).findByStreetName(anyString());
    verify(this.streetRepo, times(0)).findByCep(anyString());
    verify(this.streetRepo, times(0)).save(any(Street.class));
  }

  @Test
  @DisplayName("Add address test: should change principal address to the new address")
  void should_change_principal_id_to_the_new_one() throws Exception {
    List<Address> addressList = new ArrayList<>();
    addressList.add(this.address);
    when(this.personService.findPersonById(Long.valueOf(1))).thenReturn(this.person);
    when(this.addressRepo.findAllByPerson(person)).thenReturn(addressList);

    AddressDto newAddressDto = new AddressDto();
    newAddressDto.setCep(AddressDataExample.getCep());
    newAddressDto.setCity(AddressDataExample.getCityName());
    newAddressDto.setNumber(2);
    newAddressDto.setPrincipal(true);
    newAddressDto.setStreet(AddressDataExample.getStreetName());

    Address newAddress = new Address();
    newAddress.setId(Long.valueOf(2));
    newAddress.setNumber(2);
    newAddress.setPerson(this.person);
    newAddress.setStreet(this.street);
    newAddress.setPrincipal(true);

    this.findCityStreet(this.city, this.street);
    this.saveAddressMock(newAddress);
    Address addressAdded = this.service.addNewAddress(newAddressDto, Long.valueOf(1));

    verify(this.addressRepo, times(1)).findAllByPerson(this.person);
    verify(this.cityRepo, times(1)).findByCityName(anyString());
    verify(this.cityRepo, times(0)).save(any(City.class));
    verify(this.streetRepo, times(1)).findByStreetName(anyString());
    verify(this.streetRepo, times(1)).findByCep(anyString());
    verify(this.streetRepo, times(0)).save(any(Street.class));
    verify(this.addressRepo, times(2)).save(any(Address.class));
    assertEquals(newAddress, addressAdded);
  }

  @Test
  @DisplayName("Find Address by person tests: should return Address list from person")
  void should_return_address_list_of_the_person() {
    List<Address> addressList = new ArrayList<>();
    addressList.add(this.address);
    when(this.personService.findPersonById(Long.valueOf(1))).thenReturn(this.person);
    when(this.addressRepo.findAllByPerson(this.person)).thenReturn(addressList);

    List<Address> addresses = this.service.findAllAddressByPersonId(Long.valueOf(1));

    assertEquals(1, addresses.size());
    assertEquals(addressList, addresses);
    verify(this.addressRepo, times(1)).findAllByPerson(any(Person.class));
    verify(this.personService, times(1)).findPersonById(anyLong());
  }

  @Test
  @DisplayName("Find by id: Should throw if address not find")
  void should_throw_if_address_not_found() throws Exception {
    Throwable exception = assertThrows(AddressNotFoundException.class, () -> {
      this.service.findAddresById(Long.valueOf(1));
    });

    assertNotNull(exception);
    assertEquals(ErrorMessages.AddressNotFound, exception.getMessage());
    verify(this.addressRepo, times(1)).getReferenceById(Long.valueOf(1));
  }

  @Test
  @DisplayName("Find by id: Should return the selected address")
  void should_return_the_selected_address() throws Exception {
    when(this.addressRepo.getReferenceById(anyLong())).thenReturn(this.address);
    Address address = this.service.findAddresById(Long.valueOf(1));

    assertNotNull(address);
    assertEquals(this.address, address);
    verify(this.addressRepo, times(1)).getReferenceById(Long.valueOf(1));
  }

  @Test
  void test() {
    Address addressToPrincipal = new Address();
    addressToPrincipal.setNumber(2);
    addressToPrincipal.setPrincipal(false);
    addressToPrincipal.setId(Long.valueOf(2));
    addressToPrincipal.setPerson(this.person);
    addressToPrincipal.setStreet(this.street);

    List<Address> addressList = new ArrayList<>();
    addressList.add(this.address);
    addressList.add(addressToPrincipal);

    when(this.addressRepo.getReferenceById(Long.valueOf(2))).thenReturn(addressToPrincipal);
    when(this.addressRepo.findAllByPerson(this.person)).thenReturn(addressList);
    when(this.addressRepo.save(this.address)).thenReturn(this.address);
    when(this.addressRepo.save(addressToPrincipal)).thenReturn(addressToPrincipal);

    Address addressEdited = this.service.changePrincipalAddress(Long.valueOf(2));

    verify(this.addressRepo, times(1)).getReferenceById(Long.valueOf(2));
    verify(this.addressRepo, times(1)).findAllByPerson(any(Person.class));
    verify(this.addressRepo, times(1)).save(this.address);
    verify(this.addressRepo, times(1)).save(addressToPrincipal);
    assertEquals(true, addressEdited.isPrincipal());
  }

  private void saveCityMock() {
    when(this.cityRepo.save(any(City.class))).thenReturn(this.city);
  }

  private void saveStreetMock() {
    when(this.streetRepo.save(any(Street.class))).thenReturn(this.street);
  }

  private void saveAddressMock(Address addressReturn) {
    when(this.addressRepo.save(any(Address.class))).thenReturn(addressReturn);
  }

  private void findCityStreet(City city, Street street) {
    when(this.cityRepo.findByCityName(anyString())).thenReturn(city);
    when(this.streetRepo.findByStreetName(this.addressDto.getStreet())).thenReturn(street);
    when(this.streetRepo.findByCep(this.addressDto.getCep())).thenReturn(street);
  }
}

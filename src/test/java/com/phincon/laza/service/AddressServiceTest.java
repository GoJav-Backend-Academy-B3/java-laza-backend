package com.phincon.laza.service;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.City;
import com.phincon.laza.model.entity.Province;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.AddressRepository;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.repository.ProvinceRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.impl.AddressServiceImpl;
import com.phincon.laza.service.impl.RajaongkirServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ProvinceRepository provinceRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
   private AddressServiceImpl addressService;

    @BeforeEach
    public void init() {
        User user = new User();
        user.setId("1");

        Province province = new Province();
        province.setProvinceId("1");
        province.setProvince("DKI Jakarta");

        City city = new City();
        city.setCityId("1");
        city.setCityName("Jakarta Selatan");
        city.setProvinces(province);

        Address address = new Address();
        address.setId(1L);
        address.setReceiverName("ari");
        address.setFullAddress("Jln. Jalan");
        address.setPhoneNumber("123123");
        address.setCity(city);
        address.setReceiverName("ari");
        address.setPrimary(true);
        address.setUser(user);

        Address address1 = new Address();
        address1.setUser(user);
        address1.setId(2L);

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        addresses.add(address1);

        lenient().when(userRepository.findById("1")).thenReturn(Optional.of(user));
        lenient().when(provinceRepository.findByProvinceIgnoreCase("DKI Jakarta")).thenReturn(Optional.of(province));
        lenient().when(cityRepository.findByCityNameIgnoreCaseAndProvincesProvinceId("Jakarta Selatan", "1")).thenReturn(Optional.of(city));

        lenient().when(addressRepository.save(any(Address.class))).thenReturn(address);
        lenient().when(addressRepository.findAllByUserId("1")).thenReturn(addresses);
        lenient().when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        lenient().when(addressRepository.countByUserId("1")).thenReturn(2);
    }

    @Test
    @DisplayName("Add address when address count is 0 and is primary true")
    public void whenAddRequestToAddress_thenCorrectResponse() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta Selatan");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        Address address = addressService.add("1", addressRequest);
        assertEquals("ari", address.getReceiverName());
        assertTrue(address.isPrimary());

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("Add address when address request primary is true")
    public void whenAddRequestToAddressPrimaryIsTrue_thenCorrectResponse() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta Selatan");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(true);

        Address address = addressService.add("1", addressRequest);
        assertEquals("ari", address.getReceiverName());
        assertTrue(address.isPrimary());

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("Add address when user not found")
    public void whenAddRequestToAddressAndUserNotFound_thenThrowException() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta selatan");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        assertThrows(NotFoundException.class, () -> {
            addressService.add("2", addressRequest);
        });
    }

    @Test
    @DisplayName("Add address when province not found")
    public void whenAddRequestToAddressAndProvinceNotFound_thenThrowException() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta selatan");
        addressRequest.setProvinceName("DKI");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        assertThrows(NotFoundException.class, () -> {
            addressService.add("1", addressRequest);
        });
    }

    @Test
    @DisplayName("Add address when city not found")
    public void whenAddRequestToAddressAndCityNotFound_thenThrowException() {
        when(cityRepository.findByCityNameIgnoreCaseAndProvincesProvinceId(anyString(), anyString())).thenReturn(Optional.empty());

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        assertThrows(NotFoundException.class, () -> {
            addressService.add("1", addressRequest);
        });
    }

    @Test
    @DisplayName("find all address")
    public void whenFindAllAddressByUserId_thenCorrectResponse() {
        List<Address> allAddress = addressService.findAllByUserId("1");
        assertEquals(2, allAddress.size());

        verify(addressRepository, times(1)).findAllByUserId(anyString());
    }

    @Test
    @DisplayName("find all address when user not found")
    public void whenFindAllAddressAndUserNotFound_thenThrowException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> addressService.findAllByUserId("1"));
    }

    @Test
    @DisplayName("find by id")
    public void whenFindId_thenCorrectResponse() throws Exception {
       Address address = addressService.findById(1L);
        assertEquals(1L, address.getId());

        verify(addressRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("find by id when id not found")
    public void whenFindByIdAndIdNotFound_thenThrowException() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> addressService.findById(4L));
    }

    @Test
    @DisplayName("update address")
    public void whenUpdateAddress_thenCorrectResponse() throws Exception {
        AddressRequest updatedAddressRequest = new AddressRequest();
        updatedAddressRequest.setFullAddress("Updated Address");
        updatedAddressRequest.setPhone("987654321");
        updatedAddressRequest.setCityName("Jakarta Selatan");
        updatedAddressRequest.setProvinceName("DKI Jakarta");
        updatedAddressRequest.setReceiverName("Updated Receiver");
        updatedAddressRequest.setPrimary(true);

        Address address = addressService.update("1", 1L, updatedAddressRequest);

        verify(addressRepository, times(1)).save(address);
    }

    @Test
    @DisplayName("update address when user id not found")
    public void whenUpdateAddressAndUserNotFound_thenThrowException() throws Exception {
        AddressRequest updatedAddressRequest = new AddressRequest();
        updatedAddressRequest.setFullAddress("Updated Address");
        updatedAddressRequest.setPhone("987654321");
        updatedAddressRequest.setCityName("Jakarta Selatan");
        updatedAddressRequest.setProvinceName("DKI Jakarta");
        updatedAddressRequest.setReceiverName("Updated Receiver");
        updatedAddressRequest.setPrimary(true);

        assertThrows(NotFoundException.class, () -> addressService.update("2", 1L, updatedAddressRequest));

    }

    @Test
    @DisplayName("update address when address id not found")
    public void whenUpdateAddressAndAddressNotFound_thenThrowException() throws Exception {
        AddressRequest updatedAddressRequest = new AddressRequest();
        updatedAddressRequest.setFullAddress("Updated Address");
        updatedAddressRequest.setPhone("987654321");
        updatedAddressRequest.setCityName("Jakarta Selatan");
        updatedAddressRequest.setProvinceName("DKI Jakarta");
        updatedAddressRequest.setReceiverName("Updated Receiver");
        updatedAddressRequest.setPrimary(true);

        assertThrows(NotFoundException.class, () -> addressService.update("1", 2L, updatedAddressRequest));

    }

    @Test
    @DisplayName("update address when province not found")
    public void whenUpdateAddressAndProvinceNotFound_thenThrowException() throws Exception {
        AddressRequest updatedAddressRequest = new AddressRequest();
        updatedAddressRequest.setFullAddress("Updated Address");
        updatedAddressRequest.setPhone("987654321");
        updatedAddressRequest.setCityName("Jakarta Selatan");
        updatedAddressRequest.setProvinceName("DKI");
        updatedAddressRequest.setReceiverName("Updated Receiver");
        updatedAddressRequest.setPrimary(true);

        assertThrows(NotFoundException.class, () -> addressService.update("1", 1L, updatedAddressRequest));

    }

    @Test
    @DisplayName("update address when city not found")
    public void whenUpdateAddressAndCityNotFound_thenThrowException() throws Exception {
        AddressRequest updatedAddressRequest = new AddressRequest();
        updatedAddressRequest.setFullAddress("Updated Address");
        updatedAddressRequest.setPhone("987654321");
        updatedAddressRequest.setCityName("Jakarta");
        updatedAddressRequest.setProvinceName("DKI Jakarta");
        updatedAddressRequest.setReceiverName("Updated Receiver");
        updatedAddressRequest.setPrimary(true);

        assertThrows(NotFoundException.class, () -> addressService.update("1", 1L, updatedAddressRequest));

    }

    @Test
    @DisplayName("update address when address is primary true and request is primary false")
    public void whenUpdateAddressAndRequestIsPrimaryFalse_thenThrowException() throws Exception {
        AddressRequest updatedAddressRequest = new AddressRequest();
        updatedAddressRequest.setFullAddress("Updated Address");
        updatedAddressRequest.setPhone("987654321");
        updatedAddressRequest.setCityName("Jakarta Selatan");
        updatedAddressRequest.setProvinceName("DKI Jakarta");
        updatedAddressRequest.setReceiverName("Updated Receiver");
        updatedAddressRequest.setPrimary(false);

        assertThrows(BadRequestException.class, () -> addressService.update("1", 1L, updatedAddressRequest));

    }

    @Test
    @DisplayName("delete address")
    public void whenDeleteAddress_thenCorrectResponse() throws Exception {
        Address address = new Address();
        address.setId(2L);
        address.setPrimary(false);
        when(addressRepository.findById(2L)).thenReturn(Optional.of(address));

        addressService.delete(2L);

        verify(addressRepository, times(1)).delete(any(Address.class));
    }

    @Test
    @DisplayName("delete address when address not found")
    public void whenDeleteAddressAndAddressNotFound_thenThrowException() throws Exception {

        assertThrows(NotFoundException.class, () -> addressService.delete(2L));
    }

    @Test
    @DisplayName("delete address when delete primary")
    public void whenDeleteAddressAndDeletePrimary_thenThrowException() throws Exception {

        assertThrows(BadRequestException.class, () -> addressService.delete(1L));
    }


}

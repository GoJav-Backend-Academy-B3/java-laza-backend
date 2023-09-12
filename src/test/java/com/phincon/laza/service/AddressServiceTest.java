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
        address.setReceiverName("ari");
        address.setFullAddress("Jln. Jalan");
        address.setPhoneNumber("123123");
        address.setCity(city);
        address.setReceiverName("ari");
        address.setPrimary(true);

        lenient().when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        lenient().when(provinceRepository.findByProvinceIgnoreCase(anyString())).thenReturn(Optional.of(province));
        lenient().when(cityRepository.findByCityNameIgnoreCaseAndProvincesProvinceId(anyString(), anyString())).thenReturn(Optional.of(city));
        lenient().when(addressRepository.save(any(Address.class))).thenReturn(address);
    }

    @Test
    @DisplayName("Add address when address count is 0 and is primary true")
    public void whenAddRequestToAddress_thenCorrectResponse() {
        User user = new User();
        user.setId("1");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta selatan");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        Address address = addressService.add(user.getId(), addressRequest);
        assertEquals("ari", address.getReceiverName());
        assertTrue(address.isPrimary());

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("Add address when user not found")
    public void whenAddRequestToAddressAndUserNotFound_thenThrowException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta selatan");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        assertThrows(NotFoundException.class, () -> {
            addressService.add("1", addressRequest);
        });
    }

    @Test
    @DisplayName("Add address when user not found")
    public void whenAddRequestToAddressAndProvinceNotFound_thenThrowException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("Jakarta selatan");
        addressRequest.setProvinceName("DKI Jakarta");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(false);

        assertThrows(NotFoundException.class, () -> {
            addressService.add("1", addressRequest);
        });
    }
}

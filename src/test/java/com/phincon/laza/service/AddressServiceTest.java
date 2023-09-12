package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.AddressRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.impl.AddressServiceImpl;
import com.phincon.laza.service.impl.RajaongkirServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private RajaongkirService rajaongkirService = new RajaongkirServiceImpl();

    @InjectMocks
    private AddressService addressService = new AddressServiceImpl();

    @BeforeEach
    public void init() {
        User user = new User();
        user.setId("1");

        lenient().when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    }

    @Test
    public void whenAddRequestToAddress_thenCorrectResponse() {
        User user = new User();
        user.setId("1");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setFullAddress("Jln. Jalan");
        addressRequest.setPhone("123123");
        addressRequest.setCityName("city");
        addressRequest.setProvinceName("province");
        addressRequest.setReceiverName("ari");
        addressRequest.setPrimary(true);

        Address address = addressService.add(user.getId(), addressRequest);
        assertEquals(addressRequest.getProvinceName(), address.getProvinceName());
    }
}

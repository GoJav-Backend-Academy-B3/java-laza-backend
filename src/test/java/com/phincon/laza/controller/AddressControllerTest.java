package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private AddressController addressController;

    private SysUserDetails userDetail;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("1234567890");
        request.setPrimary(true);

        Address address = new Address();
        address.setId(1L);

        Address address1 = new Address();
        address1.setId(2L);

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        addresses.add(address1);

        lenient().when(addressService.add("1", request)).thenReturn(address);
        lenient().when(addressService.findAllByUserId("1")).thenReturn(addresses);

        userDetail = new SysUserDetails("1", "ari", "password",
                Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")));
    }


    @Test
    @DisplayName("Add Address Success")
    public void whenAddRequestToAddress_thenCorrectResponse() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("1234567890");
        request.setPrimary(true);

        Address address = new Address();
        address.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/address").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.id").value(address.getId()));

        // Verifikasi bahwa metode addressService.add dipanggil dengan argumen yang benar
        verify(addressService, times(1)).add("1", request);
    }

    @Test
    @DisplayName("Add Address when phone is not a number")
    public void whenAddRequestToAddressAndPhoneIsNan_thenFailedResponse() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setPhone("asd");

        Address address = new Address();
        address.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/address").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap());
    }

    @Test
    @DisplayName("Add Address when request is required")
    public void whenAddRequestToAddressAndRequestIsRequired_thenFailedResponse() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setProvinceName("");
        request.setCityName("");
        request.setFullAddress("");
        request.setReceiverName("");
        request.setPhone("");
        request.setPrimary(true);

        Address address = new Address();
        address.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/address").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap());
    }

}

package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private AddressController addressController;

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

        lenient().when(addressService.add("1", request)).thenReturn(address);
    }

    @Test
    @WithMockUser(username = "ari", password = "asd", authorities = "USER")
    public void whenAddRequestToAddress_thenCorrectResponse() throws Exception {
        User user = new User();
        user.setId("1");

        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("1234567890");
        request.setPrimary(true);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(addressService.add(user.getId(), request)).thenReturn(address);
        DataResponse<Address> responseData = new DataResponse<Address>(HttpStatus.CREATED.value(), "Success", address, null);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(responseData.getStatusCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").exists());

        verify(addressService, times(1)).add("1", request);
    }

    @Test
    @WithMockUser(username = "ari", password = "asd", authorities = "USER")
    public void testAddAddress_Success() throws Exception {
        // Simulasi data yang diperlukan
        SysUserDetails user = new SysUserDetails();
        user.setId("1");

        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("1234567890");
        request.setPrimary(true);

        Address address = new Address();
        address.setId(1L);

        when(addressService.add(user.getId(), request)).thenReturn(address);

        // Eksekusi metode yang diuji
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());

        // Verifikasi bahwa metode addressService.add dipanggil dengan argumen yang benar
        verify(addressService, times(1)).add(user.getId(), request);
    }

}

package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringJUnitConfig(AddressControllerTest.class)
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    @Autowired
    private AddressController addressController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
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

        DataResponse<Address> responseData = new DataResponse<Address>(HttpStatus.CREATED.value(), "Success", address, null);

        when(addressService.add(anyString(), any(AddressRequest.class))).thenReturn(address);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(responseData.getStatusCode()))
                .andExpect(jsonPath("$.message").value(responseData.getMessage()))
                .andExpect(jsonPath("$.data").exists());

    }
}

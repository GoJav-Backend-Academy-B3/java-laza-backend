package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.CustomExceptionHandler;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.security.jwt.JwtAuthenticationFilter;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@ActiveProfiles("test")
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private AddressController addressController;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private SysUserDetails userDetail;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    List<Address> addresses = new ArrayList<>();

    List<User> users = new ArrayList<>();


    @BeforeEach
    public void init() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        mockMvc  = MockMvcBuilders.standaloneSetup(addressController).setControllerAdvice(new CustomExceptionHandler()).build();

        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("1234567890");
        request.setPrimary(true);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setName("user");
        user.setImageUrl("Image");
        users.add(user);

        Address address = new Address();
        address.setUser(users.get(0));
        address.setId(1L);

        Address address1 = new Address();
        address1.setUser(users.get(0));
        address1.setId(2L);

        addresses.add(address);
        addresses.add(address1);

        lenient().when(addressService.add(any(), any())).thenReturn(addresses.get(0));
        lenient().when(addressService.findAllByUserId(anyString())).thenReturn(addresses);
        lenient().when(addressService.findByIdAndByUserId("1", 1L)).thenReturn(address);
        lenient().when(addressService.update(any(), any(), any())).thenReturn(address);
        lenient().doNothing().when(addressService).delete(any(), any());


        userDetail = new SysUserDetails(users.get(0).getId(), "ari", "password",
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


        mockMvc.perform(MockMvcRequestBuilders.post("/address").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty());

        // Verifikasi bahwa metode addressService.add dipanggil dengan argumen yang benar
        verify(addressService, times(1)).add(users.get(0).getId(), request);
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
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.phone").value("phone must be a number"));

    }

    @Test
    @DisplayName("Add Address when request is required")
    public void whenAddRequestToAddressAndRequestIsRequired_thenFailedResponse() throws Exception {
        AddressRequest request = new AddressRequest();
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
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.phone").value("phone number is required"))
                .andExpect(jsonPath("$.sub_error.provinceName").value("province is required"))
                .andExpect(jsonPath("$.sub_error.cityName").value("city is required"))
                .andExpect(jsonPath("$.sub_error.receiverName").value("receiver name is required"))
                .andExpect(jsonPath("$.sub_error.fullAddress").value("full address is required"));
    }



    @Test
    @DisplayName("get all Address Success")
    public void whenGetAllAddress_thenCorrectResponse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/address").with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());


        verify(addressService, times(1)).findAllByUserId(users.get(0).getId());
    }

    @Test
    @DisplayName("get by id Address Success")
    public void whenGetByAddressId_thenCorrectResponse() throws Exception {

//        Long id = addresses.get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/address/{id}", 1L).with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").exists());

        // Verifikasi bahwa metode addressService.add dipanggil dengan argumen yang benar
        verify(addressService, times(1)).findByIdAndByUserId("1", 1L);
    }

    @Test
    @DisplayName("get by id not found")
    public void whenGetByAddressIdNotFound_thenCorrectResponse() throws Exception {
        Long id = 3L;

       when(addressService.findByIdAndByUserId(any(), any())).thenThrow(new NotFoundException("Address not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/address/{id}", id).with(user(userDetail)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));


        verify(addressService, times(1)).findByIdAndByUserId("3", 3L);
    }


    @Test
    @DisplayName("Update Address Success")
    public void whenUpdateAddress_thenCorrectResponse() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("1234567890");
        request.setPrimary(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/address/{id}", 1L).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.id").exists());

        verify(addressService, times(1)).update("1", 1L, request);
    }

    @Test
    @DisplayName("Update Address when phone is nan")
    public void whenUpdateAddressAndPhoneIsNan_thenFailedResponse() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("asd");
        request.setPrimary(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/address/{id}", 1L).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.phone").value("phone must be a number"));

    }

    @Test
    @DisplayName("Update Address when request is nothing")
    public void whenUpdateAddressAndRequestIsNothing_thenFailedResponse() throws Exception {
        AddressRequest request = new AddressRequest();
        request.setPrimary(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/address/{id}", 1L).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.phone").value("phone number is required"))
                .andExpect(jsonPath("$.sub_error.provinceName").value("province is required"))
                .andExpect(jsonPath("$.sub_error.cityName").value("city is required"))
                .andExpect(jsonPath("$.sub_error.receiverName").value("receiver name is required"))
                .andExpect(jsonPath("$.sub_error.fullAddress").value("full address is required"));


        verify(addressService, times(0)).update("1", 1L, request);

    }

    @Test
    @DisplayName("update address when id not found")
    public void whenUpdateAddressAndIdNotFound_thenFailedResponse() throws Exception {
        Long id = 3L;

        AddressRequest request = new AddressRequest();
        request.setProvinceName("Province");
        request.setCityName("city");
        request.setFullAddress("Jl jln");
        request.setReceiverName("Ari");
        request.setPhone("123");
        request.setPrimary(true);

        when(addressService.update(any(), any(), any())).thenThrow(new NotFoundException("Address not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/address/{id}", id).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));


        verify(addressService, times(1)).update("3", 3L, request);
    }

    @Test
    @DisplayName("delete Address Success")
    public void whenDeleteAddress_thenCorrectResponse() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/address/{id}", id).with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"));

        verify(addressService, times(1)).delete("1", 1L);
    }

    @Test
    @DisplayName("delete Address failed")
    public void whenDeleteAddressAndIdNotFound_thenFailedResponse() throws Exception {
        Long id = 2L;
        doThrow(new NotFoundException("Address not found")).when(addressService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/address/{id}", id).with(user(userDetail)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));

        verify(addressService, times(1)).delete("2", 2L);
    }


}



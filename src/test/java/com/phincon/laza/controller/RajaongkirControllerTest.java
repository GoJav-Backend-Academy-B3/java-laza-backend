package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.CustomExceptionHandler;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.RajaongkirService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {RajaongkirController.class})
@ExtendWith(MockitoExtension.class)
@WebMvcTest
@WithMockUser
public class RajaongkirControllerTest {
    @MockBean
    private RajaongkirService rajaongkirService;

    @Autowired
    private RajaongkirController rajaongkirController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockMvc mM;
    private List<CourierResponse> courierResponses = new ArrayList<>();
    private List<CostsResponse> costs = new ArrayList<>();
    private List<CostResponse> costI = new ArrayList<>();
    private List<CostResponse> costII = new ArrayList<>();

    private SysUserDetails userDetail;

    @BeforeEach
    void setData(){
        costI.add(new CostResponse(87000, "7-8",""));
        costII.add(new CostResponse(102000, "6-7", ""));
        costs.add(new CostsResponse("OKE", "Ongkos Kirim Ekonomis", costI));
        costs.add(new CostsResponse("REG", "Layanan Reguler", costII));

        courierResponses.add(new CourierResponse("jne","Jalur Nugraha Ekakurir (JNE)",costs));
        userDetail = new SysUserDetails("23","smith", "password",
                Arrays.asList(new SimpleGrantedAuthority("USER"),new SimpleGrantedAuthority("ADMIN")));

    }

    @Test
    @DisplayName("[RajaongkirController] findCostCourierService")
    void whenFindCostCourierService_thenCorrectResponse() throws Exception{
        ROCostRequest requestBody = new ROCostRequest("501", "200", 1000, "jne");
        when(rajaongkirService.findCostCourierService(requestBody)).thenReturn(courierResponses);

        mockMvc.perform(MockMvcRequestBuilders.post("/costs")
                        .with(csrf())
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].code").value(courierResponses.get(0).getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].costs[0].cost[0].value").value(courierResponses.get(0).getCosts().get(0).getCost().get(0).getValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].costs[1].cost[0].value").value(courierResponses.get(0).getCosts().get(1).getCost().get(0).getValue()));

    }

    @Test
    @DisplayName("[RajaongkirController] findCostCourierService origin city not found")
    void whenFindCostCourierServiceOrigin_thenThrowException() throws Exception{
        ROCostRequest requestBody = new ROCostRequest("501", "200", 1000, "jne");
        when(rajaongkirService.findCostCourierService(requestBody)).thenThrow(new NotFoundException("Origin city not found"));
        this.mockMvc = MockMvcBuilders.standaloneSetup(rajaongkirController).setControllerAdvice(CustomExceptionHandler.class)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/costs")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Origin city not found"));
        mockMvc = mM;
    }


    @Test
    @DisplayName("[RajaongkirController] findCostCourierService destination city not found")
    void whenFindCostCourierServiceDestination_thenThrowException() throws Exception{
        ROCostRequest requestBody = new ROCostRequest("501", "200", 1000, "jne");
        when(rajaongkirService.findCostCourierService(requestBody)).thenThrow(new NotFoundException("Destination city not found"));

        this.mockMvc = MockMvcBuilders.standaloneSetup(rajaongkirController).setControllerAdvice(CustomExceptionHandler.class)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/costs")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Destination city not found"));
        mockMvc = mM;
    }

    @Test
    @DisplayName("[RajaongkirController] findCostCourierService bad payload")
    void whenFindCostCourierServiceBadPayload_thenThrowException() throws Exception{
        ROCostRequest requestBody = new ROCostRequest();
        this.mockMvc = MockMvcBuilders.standaloneSetup(rajaongkirController).setControllerAdvice(CustomExceptionHandler.class)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/costs")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sub_error.origin").value("origin is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sub_error.destination").value("destination is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sub_error.weight").value("weight is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sub_error.courier").value("courier is required"));

        mockMvc = mM;
    }

}

package com.phincon.laza.controller;


import com.phincon.laza.model.entity.City;
import com.phincon.laza.service.CityService;
import org.checkerframework.checker.units.qual.A;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {CityController.class})
@ExtendWith(MockitoExtension.class)
@WebMvcTest
@WithMockUser
public class CityControllerTest {

    @MockBean
    private CityService cityService;

    @Autowired
    private CityController controller;

    @Autowired
    private MockMvc mockMvc;

    private List<City> cities = new ArrayList<>();

    @BeforeEach
    void init(){
        cities.add(new City("39", null,  "Kabupaten","Bantul","55715", null));
        cities.add(new City("135", null, "Kabupaten","Gunung Kidul","55812",null));
    }

    @Test
    @DisplayName("[CityController] get request findAllCity")
    void whenGetFindAllCity_thenCorrectResponse() throws Exception{
        when(cityService.findAllCity()).thenReturn(cities);
        mockMvc.perform(MockMvcRequestBuilders.get("/cities")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].cityId").value("39"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].type").value("Kabupaten"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].cityName").value("Bantul"));
    }
}

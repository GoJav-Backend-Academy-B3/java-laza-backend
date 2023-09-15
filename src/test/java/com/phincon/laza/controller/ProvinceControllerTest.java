package com.phincon.laza.controller;


import com.phincon.laza.model.entity.Province;
import com.phincon.laza.service.ProvinceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
@ContextConfiguration(classes = {ProvinceController.class})
@ExtendWith(MockitoExtension.class)
@WebMvcTest
@WithMockUser
public class ProvinceControllerTest {
    @MockBean
    private ProvinceService provinceService;

    @Autowired
    private ProvinceController provinceController;

    @Autowired
    private MockMvc mockMvc;

    private List<Province> provinces = new ArrayList<>();
    @BeforeEach
    void init(){
        provinces.add(new Province("1", "Bali", null));
        provinces.add(new Province("2", "Bangka Belitung", null));
        provinces.add(new Province("3", "Banten", null));
        provinces.add(new Province("4", "Bengkulu", null));

    }

    @AfterEach
    void initEmpty(){
        provinces.clear();
    }

    @Test
    @DisplayName("[ProvinceController] Get request getAllProvince")
    void whenGetAllProvince_thenCorrectResponse() throws Exception{
        when(provinceService.findAllProvince()).thenReturn(provinces);

        mockMvc.perform(MockMvcRequestBuilders.get("/provinces")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].provinceId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.*",hasSize(4)));
    }

}

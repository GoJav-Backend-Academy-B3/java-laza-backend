package com.phincon.laza.service;


import com.phincon.laza.model.entity.City;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CityServiceTesting {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService = new CityServiceImpl();

    private List<City> cities = new ArrayList<>();

    @BeforeEach
    void init(){
        cities.add(new City("39", null,  "Kabupaten","Bantul","55715", null));
        cities.add(new City("135", null, "Kabupaten","Gunung Kidul","55812",null));
    }

    @Test
    @DisplayName("[CityService] findAllCity")
    void whenFindAllCity_thenCorrectResponse(){
        String provinceId = "55";
        when(cityRepository.findByProvincesProvinceId(provinceId)).thenReturn(cities);
        List<City> result = cityService.findAllCity(provinceId);
        assertEquals(cities, result);
        assertEquals(cities.get(0), result.get(0));
        assertEquals(cities.get(1), result.get(1));
        assertEquals("39", result.get(0).getCityId());
        assertEquals("135", result.get(1).getCityId());
        assertEquals("Bantul", result.get(0).getCityName());
        assertEquals("Gunung Kidul", result.get(1).getCityName());
        assertEquals("55715", result.get(0).getPostalCode());
        assertEquals("55812", result.get(1).getPostalCode());
    }
}

package com.phincon.laza.service;


import com.phincon.laza.model.entity.Province;
import com.phincon.laza.repository.ProvinceRepository;
import com.phincon.laza.service.impl.ProvinceServiceImpl;
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
public class ProvinceServiceTest {
    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private ProvinceService provinceService = new ProvinceServiceImpl();

    private List<Province> provinces = new ArrayList<>();

    @BeforeEach
    void init(){
        provinces.add(new Province("1", "Bali", null));
        provinces.add(new Province("2", "Bangka Belitung", null));
        provinces.add(new Province("3", "Banten", null));
        provinces.add(new Province("4", "Bengkulu", null));
    }

    @Test
    @DisplayName("[ProvinceService] find all province")
    void whenFindAllProvince_thenCorrectResponse(){
        when(provinceRepository.findAll()).thenReturn(provinces);
        List<Province> result = provinceService.findAllProvince();
        assertEquals(provinces, result);
        assertEquals(4, result.size());
        assertEquals(provinces.get(0).getProvinceId(), result.get(0).getProvinceId());
        assertEquals(provinces.get(0).getProvince(), result.get(0).getProvince());
        assertEquals(provinces.get(1), result.get(1));
        assertEquals("Bengkulu", result.get(3).getProvince());
    }
}

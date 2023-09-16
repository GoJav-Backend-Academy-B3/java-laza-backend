package com.phincon.laza.service;


import com.phincon.laza.controller.CartController;
import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.service.impl.RajaongkirServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@AutoConfigureMockMvc
@ContextConfiguration(classes = {RajaongkirServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class RajaongkirServiceTest {

    @Autowired
    private RajaongkirService rajaongkirService = new RajaongkirServiceImpl();

    @Test
    @DisplayName("[RajaongkirServiceTest] get all province")
    void whenGetAllProvince_thenReturnProvinces(){
        List<ProvinceResponse> provinceResponses = rajaongkirService.findAllProvince();
        final int expectedNumberProvince= 34;

        assertNotNull(provinceResponses);
        assertEquals(expectedNumberProvince, provinceResponses.size());
        assertEquals("Bali", provinceResponses.get(0).getProvince());
        assertEquals("1", provinceResponses.get(0).getProvince_id());
    }

    @Test
    @DisplayName("[RajaongkirServiceTest] get all city")
    void whenGetAllCityByProvinceId_thenReturnCity(){
        // 2 is Bangka Belitung
        // 34 is Sumatera Utara

        List<CityResponse> allCityResponseBangkaBelitung = rajaongkirService.findCityByProvinceId("2");
        List<CityResponse> allCityResponseSumateraUtara = rajaongkirService.findCityByProvinceId("34");

        final int expectedNumberCityBangkaBelitung = 7;
        final int expectedNumberCitySumateraUtara = 33;

        assertNotNull(allCityResponseBangkaBelitung);
        assertEquals(expectedNumberCityBangkaBelitung,allCityResponseBangkaBelitung.size());
        assertNotNull(allCityResponseSumateraUtara);
        assertEquals(expectedNumberCitySumateraUtara, allCityResponseSumateraUtara.size());

    }

    @Test
    @DisplayName("[RajaongkirServiceTest] get cost service")
    void whenGetCostService_thenReturnCost() throws Exception{
        ROCostRequest request = new ROCostRequest(
                "501",
                "200",
                1700,
                "jne"
        );
        List<CourierResponse> response = rajaongkirService.findCostCourierService(
                request
        );
        List<CostsResponse> courierService = response.get(0).getCosts();
        List<CostResponse> costs = courierService.get(0).getCost();

        assertNotNull(response);
        assertNotNull(courierService);
        assertNotNull(costs);
        assertNotNull(costs.get(0).getValue());
    }

    @Test
    @DisplayName("[RajaongkirServiceTest] get cost with invalid request")
    void whenGetCostWithInvalidRequest_thenThrowError() throws Exception{
        ROCostRequest request = new ROCostRequest(
                "501",
                "200",
                1700,
                "unKnow"
        );
        ROCostRequest requestI = new ROCostRequest(
                "501",
                "200",
                170000,
                "jne"
        );

        assertThrows(BadRequestException.class, ()->{
            rajaongkirService.findCostCourierService(request);
        });
        assertThrows(BadRequestException.class, ()->{
            rajaongkirService.findCostCourierService(requestI);
        });
    }
}

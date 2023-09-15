package com.phincon.laza.repository;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RajaongkirRepositoryTest {
    @Autowired
    private RajaongkirRepository rajaongkirRepository;

    @Test
    @DisplayName("[RajaongkirRepository] get all province")
    void getAllProvince(){
        AllProvinceResponse allProvinceResponse = rajaongkirRepository.findAllProvince();
        final int expectedNumberProvince= 34;

        assertNotNull(allProvinceResponse);
        assertEquals( 200,allProvinceResponse.getStatus().getCode());
        assertEquals("OK", allProvinceResponse.getStatus().getDescription());
        assertEquals(expectedNumberProvince, allProvinceResponse.getResults().size());
        assertEquals("Bali", allProvinceResponse.getResults().get(0).getProvince());
        assertEquals("1", allProvinceResponse.getResults().get(0).getProvince_id());
    }

    @Test
    @DisplayName("[RajaongkirRepository] get all city")
    void getAllCity(){
        // 2 is Bangka Belitung
        // 34 is Sumatera Utara

        AllCityResponse allCityResponseBangkaBelitung = rajaongkirRepository.findCityByProvinceId("2");
        AllCityResponse allCityResponseSumateraUtara = rajaongkirRepository.findCityByProvinceId("34");

        final int expectedNumberCityBangkaBelitung = 7;
        final int expectedNumberCitySumateraUtara = 33;

        assertNotNull(allCityResponseBangkaBelitung);
        assertEquals(expectedNumberCityBangkaBelitung,allCityResponseBangkaBelitung.getResults().size());
        assertEquals(200, allCityResponseBangkaBelitung.getStatus().getCode());

        assertNotNull(allCityResponseSumateraUtara);
        assertEquals(expectedNumberCitySumateraUtara, allCityResponseSumateraUtara.getResults().size());
        assertEquals(200, allCityResponseSumateraUtara.getStatus().getCode());
    }

    @Test
    @DisplayName("[RajaongkirRepository] get cost service")
    void getCostService() throws Exception{
        ROCostRequest request = new ROCostRequest(
                "501",
                "200",
                1700,
                "jne"
        );
        AllCostResponse response = rajaongkirRepository.findCostCourierService(
                request
        );
        List<CourierResponse> courier = response.getResults();
        List<CostsResponse> courierService = courier.get(0).getCosts();
        List<CostResponse> costs = courierService.get(0).getCost();

        assertNotNull(response);
        assertEquals("501", response.getOrigin_details().getCity_id());
        assertEquals("200", response.getDestination_details().getCity_id());
        assertNotNull(courier);
        assertNotNull(courierService);
        assertEquals("jne", courier.get(0).getCode());
        assertNotNull(costs);
        assertNotNull(costs.get(0).getValue());
        assertEquals(200, response.getStatus().getCode());
    }

    @Test
    @DisplayName("[RajaongkirRepository] get cost with invalid request")
    void getCostWithInvalidRequest() throws Exception{
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
            rajaongkirRepository.findCostCourierService(request);
        });
        assertThrows(BadRequestException.class, ()->{
            rajaongkirRepository.findCostCourierService(requestI);
        });
    }
}

package com.phincon.laza.service;


import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.service.impl.RajaongkirServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
public class RajaongkirServiceTest {

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
    @DisplayName("[RajaongkirServiceTest] get cost with invalid request (courier)")
    void whenGetCostWithInvalidRequestCourier_thenThrowException() throws Exception{
        ROCostRequest request = new ROCostRequest(
                "501",
                "200",
                1700,
                "unKnow"
        );

        assertThrows(BadRequestException.class, ()->{
            rajaongkirService.findCostCourierService(request);
        });
    }


    @Test
    @DisplayName("[RajaongkirServiceTest] get cost with invalid request (weight)")
    void whenGetCostWithInvalidRequestWeight_thenThrowException() throws Exception{
        ROCostRequest requestI = new ROCostRequest(
                "501",
                "200",
                170000,
                "jne"
        );

        assertThrows(BadRequestException.class, ()->{
            rajaongkirService.findCostCourierService(requestI);
        });
    }

    @Test
    @DisplayName("[RajaongkirServiceTest] get cost with invalid request (origin ID)")
    void whenGetCostWithInvalidRequestOrigin_thenThrowException() throws Exception{
        String invalidOriginId= "502";

        ROCostRequest requestI = new ROCostRequest(
                invalidOriginId,
                "200",
                1700,
                "jne"
        );

        assertThrows(BadRequestException.class, ()->{
            rajaongkirService.findCostCourierService(requestI);
        });
    }

    @Test
    @DisplayName("[RajaongkirServiceTest] get cost with invalid request (destination ID)")
    void whenGetCostWithInvalidRequestDestination_thenThrowException() throws Exception{
        String invalidDestination= "502";

        ROCostRequest requestI = new ROCostRequest(
                "501",
                invalidDestination,
                1700,
                "jne"
        );

        assertThrows(BadRequestException.class, ()->{
            rajaongkirService.findCostCourierService(requestI);
        });
    }
}

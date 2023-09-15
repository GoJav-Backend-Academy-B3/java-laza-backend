package com.phincon.laza.service;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.entity.City;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.repository.RajaongkirRepository;
import com.phincon.laza.repository.impl.RajaongkirRepositoryImpl;
import com.phincon.laza.service.impl.RajaongkirServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RajaongkirServiceTest {
    @Mock
    private RajaongkirRepository rajaongkirRepository = new RajaongkirRepositoryImpl();

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private RajaongkirService rajaongkirService = new RajaongkirServiceImpl();

    private AllCostResponse allCostResponse = new AllCostResponse();
    private List<CityResponse> cityResponses = new ArrayList<>();
    private List<CourierResponse> courierResponses = new ArrayList<>();
    private List<CostsResponse> costs = new ArrayList<>();
    private List<CostResponse> costI = new ArrayList<>();
    private List<CostResponse> costII = new ArrayList<>();

    @BeforeEach
    void init(){
        StatusResponse statusResponse = new StatusResponse(200, "OK");



        cityResponses.add(new CityResponse("39", "5", "DI Yogyakarta", "Kabupaten","Bantul","55715"));
        cityResponses.add(new CityResponse("135", "5", "DI Yogyakarta", "Kabupaten","Gunung Kidul","55812"));
        cityResponses.add(new CityResponse("200", "30", "Sulawesi Tenggara", "Kabupaten","Konawe","93411"));
        cityResponses.add(new CityResponse("210", "5", "DI Yogyakarta", "Kabupaten","Kulon Progo","55611"));
        cityResponses.add(new CityResponse("419", "5", "DI Yogyakarta", "Kabupaten","Sleman","55513"));
        cityResponses.add(new CityResponse("501", "5", "DI Yogyakarta", "Kabupaten","Yogyakarta","55111"));


        ROCostRequest costRequestBody = new ROCostRequest("501", "200", 1000, "jne");


        costI.add(new CostResponse(87000, "7-8",""));
        costII.add(new CostResponse(102000, "6-7", ""));
        costs.add(new CostsResponse("OKE", "Ongkos Kirim Ekonomis", costI));
        costs.add(new CostsResponse("REG", "Layanan Reguler", costII));

        courierResponses.add(new CourierResponse("jne","Jalur Nugraha Ekakurir (JNE)",costs));

        allCostResponse.setQuery(costRequestBody);
        allCostResponse.setResults(courierResponses);
        allCostResponse.setStatus(statusResponse);
        allCostResponse.setOrigin_details(cityResponses.get(5));
        allCostResponse.setDestination_details(cityResponses.get(2));
    }
    @AfterEach
    void cleanData(){
        cityResponses.clear();
        courierResponses.clear();
        costI.clear();
        costII.clear();
        costs.clear();
    }

    @Test
    @DisplayName("[RajaongkirService] findCostCourierService")
    void whenFindCostCourierService_thenReturnCostCourier() throws Exception{
        ROCostRequest costRequestBody = new ROCostRequest("501", "200", 1000, "jne");
        City city = new City(cityResponses.get(0).getCity_id(),null,cityResponses.get(0).getProvince_id(),cityResponses.get(0).getCity_name(),cityResponses.get(0).getPostal_code(),null);

        when(cityRepository.findById(costRequestBody.getOrigin())).thenReturn(Optional.of(city));
        when(cityRepository.findById(costRequestBody.getDestination())).thenReturn(Optional.of(city));
        when(rajaongkirRepository.findCostCourierService(costRequestBody)).thenReturn(allCostResponse);

        List<CourierResponse> courierResponses_result = rajaongkirService.findCostCourierService(costRequestBody);
        assertEquals(allCostResponse.getResults(), courierResponses_result);
        assertEquals(87000, courierResponses_result.get(0).getCosts().get(0).getCost().get(0).getValue());
        assertEquals("7-8", courierResponses_result.get(0).getCosts().get(0).getCost().get(0).getEtd());
        assertEquals("", courierResponses_result.get(0).getCosts().get(0).getCost().get(0).getNote());

        assertEquals(102000, courierResponses_result.get(0).getCosts().get(1).getCost().get(0).getValue());
        assertEquals("6-7", courierResponses_result.get(0).getCosts().get(1).getCost().get(0).getEtd());
        assertEquals("", courierResponses_result.get(0).getCosts().get(1).getCost().get(0).getNote());
    }

    @Test
    @DisplayName("[RajaongkirService] findCostCourierService origin city not found and should throw NotFoundException")
    void whenFindCostCourierServiceOrigin_thenThrowException() throws Exception{
        ROCostRequest costRequestBody = new ROCostRequest("505", "200", 1000, "jne");
        when(cityRepository.findById(costRequestBody.getOrigin())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,()->
                rajaongkirService.findCostCourierService(costRequestBody)
        );
    }

    @Test
    @DisplayName("[RajaongkirService] findCostCourierService destination city not found and should throw NotFoundException")
    void whenFindCostCourierServiceDestination_thenThrowException() throws Exception{
        ROCostRequest costRequestBody = new ROCostRequest("501", "505", 1000, "jne");
        City city = new City(cityResponses.get(0).getCity_id(),null,cityResponses.get(0).getProvince_id(),cityResponses.get(0).getCity_name(),cityResponses.get(0).getPostal_code(),null);
        when(cityRepository.findById(costRequestBody.getOrigin())).thenReturn(Optional.of(city));
        when(cityRepository.findById(costRequestBody.getDestination())).thenThrow(new NotFoundException("Destination city not found"));
        assertThrows(NotFoundException.class,()->
                rajaongkirService.findCostCourierService(costRequestBody)
        );
    }
}

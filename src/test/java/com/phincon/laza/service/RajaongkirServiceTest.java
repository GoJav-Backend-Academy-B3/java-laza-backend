package com.phincon.laza.service;

import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.repository.RajaongkirRepository;
import com.phincon.laza.repository.impl.RajaongkirRepositoryImpl;
import com.phincon.laza.service.impl.RajaongkirServiceImpl;
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
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RajaongkirServiceTest {
    @Mock
    private RajaongkirRepository rajaongkirRepository = new RajaongkirRepositoryImpl();

    @InjectMocks
    private RajaongkirService rajaongkirService = new RajaongkirServiceImpl();

    private AllProvinceResponse allProvinceResponse = new AllProvinceResponse();
    private AllCityResponse allCityResponse = new AllCityResponse();
    private AllCostResponse allCostResponse = new AllCostResponse();
    private List<ProvinceResponse> provinceResponses = new ArrayList<>();
    private List<CityResponse> cityResponses = new ArrayList<>();
    private List<CourierResponse> courierResponses = new ArrayList<>();

    @BeforeEach
    void setData(){
        StatusResponse statusResponse = new StatusResponse(200, "OK");
        provinceResponses.add(new ProvinceResponse("1", "Bali"));
        provinceResponses.add(new ProvinceResponse("2", "Bangka Belitung"));
        provinceResponses.add(new ProvinceResponse("3", "Banten"));
        provinceResponses.add(new ProvinceResponse("4", "Bengkulu"));

        Optional query = Optional.empty();
        allProvinceResponse.setQuery(query);
        allProvinceResponse.setStatus(statusResponse);
        allProvinceResponse.setResults(provinceResponses);

        cityResponses.add(new CityResponse("39", "5", "DI Yogyakarta", "Kabupaten","Bantul","55715"));
        cityResponses.add(new CityResponse("135", "5", "DI Yogyakarta", "Kabupaten","Gunung Kidul","55812"));
        cityResponses.add(new CityResponse("210", "5", "DI Yogyakarta", "Kabupaten","Kulon Progo","55611"));
        cityResponses.add(new CityResponse("419", "5", "DI Yogyakarta", "Kabupaten","Sleman","55513"));
        cityResponses.add(new CityResponse("501", "5", "DI Yogyakarta", "Kabupaten","Yogyakarta","55111"));

        allCityResponse.setQuery(query);
        allCityResponse.setResults(cityResponses);
        allCityResponse.setStatus(statusResponse);

    }

}

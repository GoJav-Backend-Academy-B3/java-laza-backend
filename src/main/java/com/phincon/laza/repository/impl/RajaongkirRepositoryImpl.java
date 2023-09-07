package com.phincon.laza.repository.impl;

import com.phincon.laza.config.RajaongkirConfig;
import com.phincon.laza.model.dto.rajaongkir.AllCityResponse;
import com.phincon.laza.model.dto.rajaongkir.AllProvinceResponse;
import com.phincon.laza.model.dto.rajaongkir.CostResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.repository.RajaongkirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


@Repository
public class RajaongkirRepositoryImpl implements RajaongkirRepository {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RajaongkirConfig rajaongkirConfig;

    @Value("${rajaongkir.province.url}")
    private String RAJAONGKIR_PROVINCE_URL;

    @Value("${rajaongkir.city.url}")
    private String RAJAONGKIR_CITY_URL;

    @Value("${rajaongkir.cost.url}")
    private String RAJAONGKIR_COST_URL;


    @Override
    public AllProvinceResponse findAllProvince() {
        HttpEntity entity = rajaongkirConfig.headerConfig("");
        ResponseEntity<Map<String, AllProvinceResponse>> result_province = restTemplate.exchange(
                RAJAONGKIR_PROVINCE_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, AllProvinceResponse>>() {
                }
        );
        AllProvinceResponse response =  result_province.getBody().get("rajaongkir");
        return response;
    }

    @Override
    public AllCityResponse findCityByProvinceId(String provinceId){
        HttpEntity entity = rajaongkirConfig.headerConfig("");
        String Url = RAJAONGKIR_CITY_URL+"?province="+provinceId;

        ResponseEntity<Map<String, AllCityResponse>> result_city = restTemplate.exchange(
                Url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, AllCityResponse>>() {
                }
        );
        AllCityResponse response = result_city.getBody().get("rajaongkir");
        return response;
    }

    @Override
    public Boolean existsCity(String cityId){
        HttpEntity entity = rajaongkirConfig.headerConfig("");
        String Url = RAJAONGKIR_CITY_URL+"?id="+cityId;
        ResponseEntity<Map<String, AllCityResponse>> result_city = restTemplate.exchange(
                Url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );
        if (result_city.getBody().get("rajaongkir").getResults().get().equals(new ArrayList<>())){
            return false;
        }
        return true;
    }

    @Override
    public CostResponse findCostCourierService(ROCostRequest roCostRequest) throws Exception {
        try{
            String data = URLEncoder.encode("origin","UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getOrigin(), "UTF-8")
                    +"&"+URLEncoder.encode("destination", "UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getDestination(), "UTF-8")
                    +"&"+URLEncoder.encode("weight", "UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getWeight().toString(), "UTF-8")
                    +"&"+URLEncoder.encode("courier","UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getCourier(),"UTF-8");
            HttpEntity entity = rajaongkirConfig.headerConfig(data);
            ResponseEntity<Map<String, CostResponse>> result = restTemplate.exchange(
                    RAJAONGKIR_COST_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return result.getBody().get("rajaongkir");
        }catch (IOException e){
            throw e;
        }
    }
}

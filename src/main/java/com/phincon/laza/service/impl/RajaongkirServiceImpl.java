package com.phincon.laza.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.config.RajaongkirConfig;
import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.service.RajaongkirService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
public class RajaongkirServiceImpl implements RajaongkirService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RajaongkirConfig rajaongkirConfig;

    @Value("${com.phincon.laza.rajaongkir.province.url}")
    private String RAJAONGKIR_PROVINCE_URL;

    @Value("${com.phincon.laza.rajaongkir.city.url}")
    private String RAJAONGKIR_CITY_URL;

    @Value("${com.phincon.laza.rajaongkir.cost.url}")
    private String RAJAONGKIR_COST_URL;

    @Autowired
    private CityRepository cityRepository;


    @Override
    public List<CourierResponse> findCostCourierService(ROCostRequest roCostRequest) throws Exception{
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
            ResponseEntity<Map<String, AllCostResponse>> result = restTemplate.exchange(
                    RAJAONGKIR_COST_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return result.getBody().get("rajaongkir").getResults();
        }catch (IOException e){
            throw e;
        }catch (HttpClientErrorException e){
            ObjectMapper newMap = new ObjectMapper();
            AllErrorResponse error = newMap.readValue(e.getResponseBodyAsString(), AllErrorResponse.class);
            if (error.getRajaongkir().getStatus().getCode().equals(400)){
                throw new BadRequestException(error.getRajaongkir().getStatus().getDescription());
            }else{
                throw new ExecutionControl.InternalException(error.getRajaongkir().getStatus().getDescription());
            }
        }
    }
    @Override
    public List<ProvinceResponse> findAllProvince() {
        HttpEntity entity = rajaongkirConfig.headerConfig("");
        ResponseEntity<Map<String, AllProvinceResponse>> result_province = restTemplate.exchange(
                RAJAONGKIR_PROVINCE_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, AllProvinceResponse>>() {
                }
        );
        AllProvinceResponse response =  result_province.getBody().get("rajaongkir");
        return response.getResults();
    }

    @Override
    public List<CityResponse> findCityByProvinceId(String provinceId){
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
        return response.getResults();
    }

}

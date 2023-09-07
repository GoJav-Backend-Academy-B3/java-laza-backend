package com.phincon.laza.repository.impl;

import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.dto.response.*;
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

    @Value("${rajaongkir.key}")
    private String RAJAONGKIR_KEY;

    @Value("${rajaongkir.province.url}")
    private String RAJAONGKIR_PROVINCE_URL;

    @Value("${rajaongkir.city.url}")
    private String RAJAONGKIR_CITY_URL;

    @Value("${rajaongkir.cost.url}")
    private String RAJAONGKIR_COST_URL;


    @Override
    public ROAllProvinceResponse findAllProvince() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("key", RAJAONGKIR_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, ROAllProvinceResponse> responseMap = new HashMap<>();
        ResponseEntity<Map<String, ROAllProvinceResponse>> result_province = restTemplate.exchange(
                RAJAONGKIR_PROVINCE_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, ROAllProvinceResponse>>() {
                }
        );
        ROAllProvinceResponse response =  result_province.getBody().get("rajaongkir");
        return response;
    }

    @Override
    public ROAllCityResponse findCityByProvinceId(String provinceId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("key", RAJAONGKIR_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String Url = RAJAONGKIR_CITY_URL+"?province="+provinceId;

        ResponseEntity<Map<String, ROAllCityResponse>> result_city = restTemplate.exchange(
                Url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, ROAllCityResponse>>() {
                }
        );
        ROAllCityResponse response = result_city.getBody().get("rajaongkir");
        return response;
    }

    @Override
    public Boolean existsProvince(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("key", RAJAONGKIR_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String Url = RAJAONGKIR_PROVINCE_URL+"?id="+id;

        ResponseEntity<Map<String, ROAllProvinceResponse>> result_province = restTemplate.exchange(
                Url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        if (result_province.getBody().get("rajaongkir").getResults().get().equals(new ArrayList<>())){
            return false;
        }
        return true;
    }

    @Override
    public Boolean existsCity(String cityId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("key", RAJAONGKIR_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String Url = RAJAONGKIR_CITY_URL+"?id="+cityId;

        ResponseEntity<Map<String, ROAllCityResponse>> result_city = restTemplate.exchange(
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
    public ROCostResponse findCostCourierService(ROCostRequest roCostRequest) throws Exception {
        try{
            String data = URLEncoder.encode("origin","UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getOrigin(), "UTF-8")
                    +"&"+URLEncoder.encode("destination", "UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getDestination(), "UTF-8")
                    +"&"+URLEncoder.encode("weight", "UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getWeight().toString(), "UTF-8")
                    +"&"+URLEncoder.encode("courier","UTF-8")
                    +"="+URLEncoder.encode(roCostRequest.getCourier(),"UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("content-type", "application/x-www-form-urlencoded");
            headers.add("key", RAJAONGKIR_KEY);
            HttpEntity<String> entity = new HttpEntity<>(data,headers);

            ResponseEntity<Map<String,ROCostResponse>> result = restTemplate.exchange(
                    RAJAONGKIR_COST_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            System.out.println("DUARR :"+result.getBody().get("rajaongkir"));
            return result.getBody().get("rajaongkir");
        }catch (IOException e){
            throw new NotProcessException("URLEncoder encode error rajaongkir");
        }
    }
}

package com.phincon.laza.service.impl;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.rajaongkir.AllCityResponse;
import com.phincon.laza.model.dto.rajaongkir.AllProvinceResponse;
import com.phincon.laza.model.dto.rajaongkir.CityResponse;
import com.phincon.laza.model.dto.rajaongkir.ProvinceResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.repository.RajaongkirRepository;
import com.phincon.laza.service.RajaongkirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.lang.reflect.Field;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RajaongkirServiceImpl implements RajaongkirService {

    @Autowired
    private RajaongkirRepository rajaongkirRepository;

    @Override
    public List<ProvinceResponse> findAllProvince() {
        AllProvinceResponse provinceResponse = rajaongkirRepository.findAllProvince();
        return provinceResponse.getResults();
    }


    @Override
    public List<CityResponse> findAllCityByProvinceId(String provinceId) {
        AllCityResponse cityResponse = rajaongkirRepository.findCityByProvinceId(provinceId);
        return cityResponse.getResults();
    }

    @Override
    public void existsProvince(String provinceName) {
        AllProvinceResponse allProvinces = rajaongkirRepository.findAllProvince();
        for (ProvinceResponse province: allProvinces.getResults()){
            if (province.getProvince().toLowerCase().equals(provinceName)){
               return;
            }
        }
        throw new NotFoundException("Province doesn't exists");
    }

    @Override
    public void existsCity(String cityName) {
        AllCityResponse allCityResponse = rajaongkirRepository.findCityByProvinceId("");
        for (CityResponse city: allCityResponse.getResults()){
            if (Objects.equals(city.getCity_name().toLowerCase(), cityName)){
                return;
            }
        }
        throw new NotFoundException("City doesn't exists");
    }

    @Override
    public Optional findCostCourierService(ROCostRequest roCostRequest) throws Exception{
        return rajaongkirRepository.findCostCourierService(roCostRequest).getResults().get(0).getCosts();
    }


}

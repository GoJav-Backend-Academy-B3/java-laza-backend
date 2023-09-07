package com.phincon.laza.service.impl;


import com.phincon.laza.model.dto.response.ROAllCityResponse;
import com.phincon.laza.model.dto.response.ROAllProvinceResponse;
import com.phincon.laza.model.dto.response.ROCityResponse;
import com.phincon.laza.model.dto.response.ROProvinceResponse;
import com.phincon.laza.repository.RajaongkirRepository;
import com.phincon.laza.service.RajaongkirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RajaongkirServiceImpl implements RajaongkirService {

    @Autowired
    private RajaongkirRepository rajaongkirRepository;

    @Override
    public List<ROProvinceResponse> findAllProvince() {
        ROAllProvinceResponse provinceResponse = rajaongkirRepository.findAllProvince();
        return provinceResponse.getResults();
    }


    @Override
    public List<ROCityResponse> findAllCityByProvinceId(String provinceId) {
        ROAllCityResponse cityResponse = rajaongkirRepository.findCityByProvinceId(provinceId);
        return cityResponse.getResults();
    }

    @Override
    public Boolean existsProvince(String id) {
        return rajaongkirRepository.existsProvince(id);
    }

    @Override
    public Boolean existsCity(String cityId) {
        return rajaongkirRepository.existsCity(cityId);
    }
}

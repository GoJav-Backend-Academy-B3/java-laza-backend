package com.phincon.laza.service.impl;


import com.phincon.laza.model.dto.rajaongkir.AllCityResponse;
import com.phincon.laza.model.dto.rajaongkir.AllProvinceResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.repository.RajaongkirRepository;
import com.phincon.laza.service.RajaongkirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RajaongkirServiceImpl implements RajaongkirService {

    @Autowired
    private RajaongkirRepository rajaongkirRepository;

    @Override
    public Optional findAllProvince() {
        AllProvinceResponse provinceResponse = rajaongkirRepository.findAllProvince();
        return provinceResponse.getResults();
    }


    @Override
    public Optional findAllCityByProvinceId(String provinceId) {
        AllCityResponse cityResponse = rajaongkirRepository.findCityByProvinceId(provinceId);
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

    @Override
    public Optional findCostCourierService(ROCostRequest roCostRequest) throws Exception{
        return rajaongkirRepository.findCostCourierService(roCostRequest).getResults().get(0).getCosts();
    }
}

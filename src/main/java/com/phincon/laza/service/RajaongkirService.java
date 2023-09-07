package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.ROCostRequest;

import java.util.Optional;

public interface RajaongkirService {
    public Optional findAllProvince();
    public Optional findAllCityByProvinceId(String provinceId);
    public Boolean existsProvince(String id);
    public Boolean existsCity(String cityId);
    public Optional findCostCourierService(ROCostRequest roCostRequest) throws Exception;
}

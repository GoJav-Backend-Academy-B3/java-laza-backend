package com.phincon.laza.service;

import com.phincon.laza.model.dto.rajaongkir.CityResponse;
import com.phincon.laza.model.dto.rajaongkir.ProvinceResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;

import java.util.List;
import java.util.Optional;

public interface RajaongkirService {
    public List<ProvinceResponse> findAllProvince();
    public Optional findAllCityByProvinceId(String provinceId);
    public boolean existsProvince(String provinceName);
    public boolean existsCity(String cityName) ;
    public Optional findCostCourierService(ROCostRequest roCostRequest) throws Exception;


}

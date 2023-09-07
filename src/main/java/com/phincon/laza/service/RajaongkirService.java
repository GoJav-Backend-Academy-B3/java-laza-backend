package com.phincon.laza.service;

import com.phincon.laza.model.dto.rajaongkir.CityResponse;
import com.phincon.laza.model.dto.rajaongkir.ProvinceResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;

import java.util.List;
import java.util.Optional;

public interface RajaongkirService {
    public List<ProvinceResponse> findAllProvince();
    public List<CityResponse> findAllCityByProvinceId(String provinceId);
    public void existsProvince(String provinceName);
    public void existsCity(String cityName) ;
    public Optional findCostCourierService(ROCostRequest roCostRequest) throws Exception;


}

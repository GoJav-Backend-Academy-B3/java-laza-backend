package com.phincon.laza.service;

import com.phincon.laza.model.dto.response.ROCityResponse;
import com.phincon.laza.model.dto.response.ROProvinceResponse;

import java.util.List;

public interface RajaongkirService {
    public List<ROProvinceResponse> findAllProvince();
    public List<ROCityResponse> findAllCityByProvinceId(String provinceId);
    public Boolean existsProvince(String id);
    public Boolean existsCity(String cityId);
}

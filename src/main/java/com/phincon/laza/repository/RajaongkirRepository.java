package com.phincon.laza.repository;


import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.dto.response.ROAllCityResponse;
import com.phincon.laza.model.dto.response.ROAllProvinceResponse;
import com.phincon.laza.model.dto.response.ROCostResponse;


public interface RajaongkirRepository {
    public ROAllProvinceResponse findAllProvince();
    public ROAllCityResponse findCityByProvinceId(String provinceId);
    public Boolean existsProvince(String provinceId);
    public Boolean existsCity(String cityId);
    public ROCostResponse findCostCourierService(ROCostRequest roCostRequest) throws Exception;

}

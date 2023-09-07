package com.phincon.laza.repository;


import com.phincon.laza.model.dto.rajaongkir.AllCityResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.dto.rajaongkir.AllProvinceResponse;
import com.phincon.laza.model.dto.rajaongkir.CostResponse;


public interface RajaongkirRepository {
    public AllProvinceResponse findAllProvince();
    public AllCityResponse findCityByProvinceId(String provinceId);
    public Boolean existsProvince(String provinceId);
    public Boolean existsCity(String cityId);
    public CostResponse findCostCourierService(ROCostRequest roCostRequest) throws Exception;

}

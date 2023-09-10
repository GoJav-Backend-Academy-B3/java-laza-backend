package com.phincon.laza.repository;


import com.phincon.laza.model.dto.rajaongkir.AllCityResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.dto.rajaongkir.AllProvinceResponse;
import com.phincon.laza.model.dto.rajaongkir.AllCostResponse;


public interface RajaongkirRepository {
    public AllProvinceResponse findAllProvince();
    public AllCityResponse findCityByProvinceId(String provinceId);
    public AllCostResponse findCostCourierService(ROCostRequest roCostRequest) throws Exception;

}

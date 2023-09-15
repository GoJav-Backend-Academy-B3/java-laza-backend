package com.phincon.laza.service;

import com.phincon.laza.model.dto.rajaongkir.CityResponse;
import com.phincon.laza.model.dto.rajaongkir.CostsResponse;
import com.phincon.laza.model.dto.rajaongkir.CourierResponse;
import com.phincon.laza.model.dto.rajaongkir.ProvinceResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;

import java.util.List;
import java.util.Optional;

public interface RajaongkirService {

    public List<CourierResponse> findCostCourierService(ROCostRequest roCostRequest) throws Exception;

}

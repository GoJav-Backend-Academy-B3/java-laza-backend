package com.phincon.laza.controller;


import com.phincon.laza.model.dto.rajaongkir.CityResponse;
import com.phincon.laza.model.dto.rajaongkir.CostsResponse;
import com.phincon.laza.model.dto.rajaongkir.CourierResponse;
import com.phincon.laza.model.dto.rajaongkir.ProvinceResponse;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.dto.response.*;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.service.RajaongkirService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@RestController
public class RajaongkirController {

    @Autowired
    private RajaongkirService rajaongkirService;

    @GetMapping("/provinces")
    public ResponseEntity<DataResponse<List<ProvinceResponse>>> findAllProvince(
    ){
        List<ProvinceResponse> provinces = rajaongkirService.findAllProvince();
        DataResponse<List<ProvinceResponse>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                provinces,
                null);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/cities")
    public ResponseEntity<DataResponse< List<CityResponse>>> findAllCity(@RequestParam(value = "province", required = false)
                                                                          String province){
        List<CityResponse> cities = rajaongkirService.findAllCityByProvinceId(province);
        DataResponse< List<CityResponse>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                cities,
                null);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/costs")
    public ResponseEntity<DataResponse<List<CourierResponse>>> findCostCourierService(@Valid @RequestBody ROCostRequest roCostRequest) throws Exception{
        List<CourierResponse> courierCost = rajaongkirService.findCostCourierService(roCostRequest);
        DataResponse<List<CourierResponse>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                courierCost,
                null);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}

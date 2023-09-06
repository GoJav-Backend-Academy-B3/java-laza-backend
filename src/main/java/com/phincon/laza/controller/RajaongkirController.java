package com.phincon.laza.controller;


import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.ROAllProvinceResponse;
import com.phincon.laza.model.dto.response.ROCityResponse;
import com.phincon.laza.model.dto.response.ROProvinceResponse;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.service.RajaongkirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.util.List;

@RestController
@RequestMapping("/rajaongkir")
public class RajaongkirController {

    @Autowired
    private RajaongkirService rajaongkirService;

    @GetMapping("/provinces")
    public ResponseEntity<DataResponse<List<ROProvinceResponse>>> findAllProvince(
    ){
        List<ROProvinceResponse> provinces = rajaongkirService.findAllProvince();
        DataResponse<List<ROProvinceResponse>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                provinces,
                null);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/cities")
    public ResponseEntity<DataResponse<List<ROCityResponse>>> findAllCity(@RequestParam(value = "province", required = false)
                                                                          String province){
        List<ROCityResponse> cities = rajaongkirService.findAllCityByProvinceId(province);
        DataResponse<List<ROCityResponse>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                cities,
                null);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}

package com.phincon.laza.controller;


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
    public ResponseEntity<DataResponse<Optional>> findAllCity(@RequestParam(value = "province", required = false)
                                                                          String province){
        Optional cities = rajaongkirService.findAllCityByProvinceId(province);
        DataResponse<Optional> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                cities,
                null);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/costs")
    public ResponseEntity<DataResponse<Optional>> findCostCourierService(@Valid @RequestBody ROCostRequest roCostRequest) throws Exception{
        Optional courierCost = rajaongkirService.findCostCourierService(roCostRequest);
        DataResponse<Optional> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "OK",
                courierCost,
                null);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/existsProvince/{provinceName}")
    public ResponseEntity<DataResponse<String>> existsProvince(@PathVariable(value = "provinceName") String id){
        String result_ = "there is no province";
        if (rajaongkirService.existsProvince(id)){
            result_ = "there is province";
        }
        DataResponse<String> result = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                result_,
                null
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/existsCity/{cityName}")
    public ResponseEntity<DataResponse<String>> existsCity(@PathVariable(value = "cityName") String id){
        String result_ = "there is no cityName";
        if (rajaongkirService.existsCity(id)){
            result_ = "there is cityName";
        }
        DataResponse<String> result = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                result_,
                null
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

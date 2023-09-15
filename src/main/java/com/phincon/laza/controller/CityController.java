package com.phincon.laza.controller;


import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.City;
import com.phincon.laza.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/cities")
    public ResponseEntity<?> findAllCity(@RequestParam(value = "provinceId", required = false) String provinceId){
        List<City> cities = cityService.findAllCity(provinceId);

        DataResponse<?> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                cities,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

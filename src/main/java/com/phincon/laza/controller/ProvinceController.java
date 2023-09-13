package com.phincon.laza.controller;

import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Province;
import com.phincon.laza.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping("/provinces")
    public ResponseEntity<Object> getAllProvince(){
        List<Province> provinces = provinceService.findAllProvince();
        DataResponse<?> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                provinces,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.phincon.laza.service.impl;


import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.rajaongkir.*;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.entity.City;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.repository.RajaongkirRepository;
import com.phincon.laza.service.RajaongkirService;
import com.phincon.laza.validator.RajaongkirValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class RajaongkirServiceImpl implements RajaongkirService {

    @Autowired
    private RajaongkirRepository rajaongkirRepository;

    @Autowired
    private CityRepository cityRepository;


    @Override
    public List<CourierResponse> findCostCourierService(ROCostRequest roCostRequest) throws Exception{

        if (cityRepository.findById(roCostRequest.getOrigin()).isEmpty()){
            throw new NotFoundException("Origin city not found");
        }

        if (cityRepository.findById(roCostRequest.getDestination()).isEmpty()){
            throw new NotFoundException("Destination city not found");
        }

        return rajaongkirRepository.findCostCourierService(roCostRequest).getResults();
    }
}

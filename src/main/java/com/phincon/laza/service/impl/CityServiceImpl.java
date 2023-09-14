package com.phincon.laza.service.impl;

import com.phincon.laza.model.entity.City;
import com.phincon.laza.repository.CityRepository;
import com.phincon.laza.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<City> findAllCity(String provinceId)  {
        if (provinceId == null){
            return  cityRepository.findAll();
        }
        return cityRepository.findByProvincesProvinceId(provinceId);
    }
}

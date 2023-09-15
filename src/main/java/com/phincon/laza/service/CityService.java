package com.phincon.laza.service;

import com.phincon.laza.model.entity.City;

import java.util.List;

public interface CityService {
    List<City> findAllCity(String provinceId);
}

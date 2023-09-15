package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Province;
import com.phincon.laza.repository.ProvinceRepository;
import com.phincon.laza.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<Province> findAllProvince() {
        return provinceRepository.findAll();
    }
}

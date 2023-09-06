package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BrandServiceImpl implements BrandService {
    @Override
    public Brand add(BrandRequest request) {
        return null;
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Brand findById(Long id) {
        return null;
    }

    @Override
    public Brand findByName(String name) {
        return null;
    }

    @Override
    public Brand update(Long id, BrandRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

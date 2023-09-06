package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.repository.BrandRepository;
import com.phincon.laza.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CloudinaryImageServiceImpl cloudinaryImageService;

    @Override
    public Brand add(BrandRequest request) {
        Optional<Brand> optionalBrand = brandRepository.findByName(request.getName());

        if (optionalBrand.isEmpty()) {
            Brand brand = new Brand();


            brand.setName(request.getName());
        }

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

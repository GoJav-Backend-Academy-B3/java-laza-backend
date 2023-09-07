package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.User;
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
    public Brand add(BrandRequest request) throws Exception {
        Optional<Brand> optionalBrand = brandRepository.findByNameAndIsDeletedFalse(request.getName());

        if (optionalBrand.isPresent()) {
            throw new ConflictException("Brand is exists");
        }

        Brand brand = new Brand();

        CloudinaryUploadResult result = cloudinaryImageService.upload(request.getLogoUrl(), "brand");

        brand.setName(request.getName().toLowerCase());
        brand.setLogoUrl(result.secureUrl());

        return brandRepository.save(brand);
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {

        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new NotFoundException("brand is doesn't exists"));
    }

    @Override
    public Brand findByName(String name) {
        return brandRepository.findByNameAndIsDeletedFalse(name).orElseThrow(() -> new NotFoundException("brand is doesn't exists"));
    }

    @Override
    public Brand update(Long id, BrandRequest request) throws Exception {
        Optional<Brand> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isPresent()) {
           Optional<Brand> brandExists = brandRepository.findByNameAndIsDeletedFalse(request.getName());

           if (brandExists.isPresent()) {
               throw new ConflictException("Brand is exists");
           }
            Brand brand = optionalBrand.get();

            CloudinaryUploadResult result = cloudinaryImageService.upload(request.getLogoUrl(), "brand");

            brand.setId(id);
            brand.setName(request.getName().toLowerCase());
            brand.setLogoUrl(result.secureUrl());

            return brandRepository.save(brand);
        }

        throw new NotFoundException("Brand doesn't exists");

    }

    @Override
    public void delete(Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);

        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();

            brand.setIsDeleted(true);
            brandRepository.save(brand);
            return;
        }

        throw new NotFoundException("Brand doesn't exists");
    }
}

package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    Brand add(BrandRequest request);

    Page<Brand> findAll(Pageable pageable);

    Brand findById(Long id);

    Brand findByName(String name);

    Brand update(Long id, BrandRequest request);

    void delete(Long id);
}

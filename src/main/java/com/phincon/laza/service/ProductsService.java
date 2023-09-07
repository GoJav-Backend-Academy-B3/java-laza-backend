package com.phincon.laza.service;


import org.springframework.stereotype.Service;

import com.phincon.laza.model.dto.request.CreateProductRequest;
import com.phincon.laza.model.entity.Product;

@Service
public interface ProductsService {
    Product getProductById(Long id) throws Exception;
    Product create(CreateProductRequest createProductRequest) throws Exception;
}

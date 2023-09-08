package com.phincon.laza.service;

import org.springframework.stereotype.Service;

import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.entity.Product;

@Service
public interface ProductsService {
    Product getProductById(Long id) throws Exception;

    Product create(CreateUpdateProductRequest createProductRequest) throws Exception;

    Product update(Long id, CreateUpdateProductRequest updateProductRequest) throws Exception;

    void delete(Long id) throws Exception;
}

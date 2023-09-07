package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.CreateProductRequest;
import com.phincon.laza.model.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductsService {
    Product getProductById(Long id) throws Exception;
    Product create(CreateProductRequest createProductRequest) throws Exception;
}

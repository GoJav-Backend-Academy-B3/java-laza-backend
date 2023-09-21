package com.phincon.laza.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.entity.Product;


@Service
public interface ProductsService {
    Page<Product> getAll(int page, int size);

    Product getProductById(Long id) throws Exception;

    Page<Product> findProductByName(String keyword, int page, int size);

    Product create(CreateUpdateProductRequest createProductRequest) throws Exception;

    Product update(Long id, CreateUpdateProductRequest updateProductRequest) throws Exception;

    void setImageUrl(Long id, String imageUrl) throws Exception;

    void delete(Long id) throws Exception;
}

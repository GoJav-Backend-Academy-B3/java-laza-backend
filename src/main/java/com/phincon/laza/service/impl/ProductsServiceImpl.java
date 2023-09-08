package com.phincon.laza.service.impl;


import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.ProductsRepository;
import com.phincon.laza.service.ProductsService;
import com.phincon.laza.service.SizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    public Product getProductById(Long id) throws Exception {
        Optional<Product> productOptional = productsRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        return productOptional.get();
    }
}

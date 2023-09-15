package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.ProductOrderDetail;
import com.phincon.laza.repository.ProductOrderDetailRepository;
import com.phincon.laza.service.ProductOrderDetailService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOrderDetailServiceImpl implements ProductOrderDetailService {

    private final ProductOrderDetailRepository productOrderDetailRepository;

    @Autowired
    public ProductOrderDetailServiceImpl(ProductOrderDetailRepository productOrderDetailRepository) {
        this.productOrderDetailRepository = productOrderDetailRepository;
    }

    @Override
    public List<ProductOrderDetail> getAllProductOrderDetails() {
        return productOrderDetailRepository.findAll();
    }

    @Override
    public ProductOrderDetail getProductOrderDetailById(String id) {
        Optional<ProductOrderDetail> productOrderDetail = productOrderDetailRepository.findById(id);
        if (productOrderDetail.isPresent()) {
            return productOrderDetail.get();
        } else {
            throw new NotFoundException("ProductOrderDetail not found");
        }
    }

    @Override
    public ProductOrderDetail createProductOrderDetail(ProductOrderDetail productOrderDetail) {
        try {
            return productOrderDetailRepository.save(productOrderDetail);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public List<ProductOrderDetail> createProductOrderDetails(List<ProductOrderDetail> productOrderDetails) {
        try {
            return productOrderDetailRepository.saveAll(productOrderDetails);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public ProductOrderDetail updateProductOrderDetail(String id, ProductOrderDetail productOrderDetail) {
        if (productOrderDetailRepository.existsById(id)) {
            productOrderDetail.setId(id);
            return productOrderDetailRepository.save(productOrderDetail);
        } else {
            throw new NotFoundException("ProductOrderDetail not found");
        }
    }

    @Override
    public void deleteProductOrderDetail(String id) {
        if (productOrderDetailRepository.existsById(id)) {
            productOrderDetailRepository.deleteById(id);
        } else {
            throw new NotFoundException("ProductOrderDetail not found");
        }
    }
}


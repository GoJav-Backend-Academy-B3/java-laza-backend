package com.phincon.laza.service;

import com.phincon.laza.model.entity.ProductOrderDetail;

import java.util.List;

public interface ProductOrderDetailService {
    List<ProductOrderDetail> getAllProductOrderDetails();

    ProductOrderDetail getProductOrderDetailById(String id);

    ProductOrderDetail createProductOrderDetail(ProductOrderDetail productOrderDetail);

    List<ProductOrderDetail> createProductOrderDetails(List<ProductOrderDetail> productOrderDetails);

    ProductOrderDetail updateProductOrderDetail(String id, ProductOrderDetail productOrderDetail);

    void deleteProductOrderDetail(String id);
}


package com.phincon.laza.service;


import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    Product getProductById(Long id) throws Exception;

}

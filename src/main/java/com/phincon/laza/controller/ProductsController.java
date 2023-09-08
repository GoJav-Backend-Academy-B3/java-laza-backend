package com.phincon.laza.controller;

import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.ProductsResponse;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Product>> getProductById(@PathVariable Long id) throws Exception {
        Product product = productsService.getProductById(id);
        DataResponse<Product> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", product, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}

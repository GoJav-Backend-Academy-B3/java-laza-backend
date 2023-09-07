package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.ProductsResponse;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<ProductsResponse>> getProductById(@PathVariable Long id) throws Exception {
        Product product = productsService.getProductById(id);
        ProductsResponse result = new ProductsResponse(product);
        DataResponse<ProductsResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", result,
                null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping
    public ResponseEntity<DataResponse<ProductsResponse>> createProduct(
            @ModelAttribute CreateUpdateProductRequest request) throws Exception {
        Product product = productsService.create(request);
        ProductsResponse result = new ProductsResponse(product);
        DataResponse<ProductsResponse> dataResponse = new DataResponse<ProductsResponse>(HttpStatus.CREATED.value(),
                "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}

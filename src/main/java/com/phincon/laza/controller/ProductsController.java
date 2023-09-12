package com.phincon.laza.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.dto.response.CreateUpdateProductResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.OverviewProductResponse;
import com.phincon.laza.model.dto.response.PaginationMeta;
import com.phincon.laza.model.dto.response.ProductsResponse;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.service.ProductsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<ProductsResponse>> getProductById(@PathVariable Long id) throws Exception {
        Product product = productsService.getProductById(id);
        ProductsResponse result = new ProductsResponse(product);
        DataResponse<ProductsResponse> dataResponse = new DataResponse<ProductsResponse>(HttpStatus.OK.value(),
                "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping
    public ResponseEntity<DataResponse<CreateUpdateProductResponse>> createProduct(
            @ModelAttribute CreateUpdateProductRequest request) throws Exception {
        Product product = productsService.create(request);
        CreateUpdateProductResponse result = CreateUpdateProductResponse.fromProductEntity(product);
        DataResponse<CreateUpdateProductResponse> dataResponse = new DataResponse<>(HttpStatus.CREATED.value(),
                "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<CreateUpdateProductResponse>> updateProduct(
            @PathVariable Long id,
            @ModelAttribute CreateUpdateProductRequest request) throws Exception {
        Product product = productsService.update(id, request);
        CreateUpdateProductResponse result = CreateUpdateProductResponse.fromProductEntity(product);
        DataResponse<CreateUpdateProductResponse> dataResponse = new DataResponse<CreateUpdateProductResponse>(
                HttpStatus.OK.value(),
                "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<DataResponse<List<OverviewProductResponse>>> searchProduct(
            @RequestParam(name = "q", required = true) String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        var productPage = productsService.findProductByName(query, page, size);
        PaginationMeta meta = new PaginationMeta(page, size, productPage.getNumberOfElements());
        var data = productPage.get()
                .map(OverviewProductResponse::fromProductEntity)
                .collect(Collectors.toList());
        return DataResponse.ok(data, meta);
    }
}

package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductsResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer price;
    private LocalDateTime createdAt;
    private CategoryResponse category;
    private List<SizeResponse> sizes;

    public ProductsResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.price = product.getPrice();
        this.createdAt = product.getCreatedAt();
        if (product.getCategory() != null) {
            this.category = new CategoryResponse(product.getCategory());
        }
//        if (product.getSizes() != null) {
//            this.sizes = product.getSizes().stream()
//                    .map(SizeResponse::new)
//                    .collect(Collectors.toList());
//        }
        this.sizes = (product.getSizes() != null) ? product.getSizes().stream()
                .map(SizeResponse::new)
                .collect(Collectors.toList()) : Collections.emptyList();
    }
}

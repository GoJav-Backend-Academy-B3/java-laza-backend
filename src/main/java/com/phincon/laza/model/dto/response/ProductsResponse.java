package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    private BrandResponse brand;
    private List<ReviewResponse> review;
    private List<Size> sizes;

    public ProductsResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.price = product.getPrice();
        this.createdAt = product.getCreatedAt();
        this.category = new CategoryResponse(product.getCategory());
        this.brand = BrandResponse.fromEntity(product.getBrand());
        this.review = product.getReviewList().stream().limit(2)
                .map(ReviewResponse::new)
                .collect(Collectors.toList());

        this.sizes = product.getSizes();
    }
}

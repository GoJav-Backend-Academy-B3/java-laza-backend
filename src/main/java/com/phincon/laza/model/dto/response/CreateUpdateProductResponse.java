package com.phincon.laza.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phincon.laza.model.entity.Product;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateUpdateProductResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        Integer price,
        LocalDateTime createdAt,
        CategoryResponse category,
        BrandResponse brand,
        List<SizeResponse> sizes) {

    public static CreateUpdateProductResponse fromProductEntity(Product product) {
        return new CreateUpdateProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getImageUrl(), product.getPrice(), product.getCreatedAt(),
                new CategoryResponse(product.getCategory()), BrandResponse.fromEntity(product.getBrand()),
                product.getSizes().stream().map(SizeResponse::new).collect(Collectors.toList()));
    }
}

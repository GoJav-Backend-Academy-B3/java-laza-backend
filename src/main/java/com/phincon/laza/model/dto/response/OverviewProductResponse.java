package com.phincon.laza.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phincon.laza.model.entity.Product;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OverviewProductResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl) {
    public static OverviewProductResponse fromProductEntity(Product p) {
        return new OverviewProductResponse(p.getId(), p.getName(), p.getPrice(), p.getImageUrl());
    }
}

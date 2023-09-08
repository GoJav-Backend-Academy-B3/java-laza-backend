package com.phincon.laza.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phincon.laza.model.entity.Brand;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BrandResponse(
        Long id,
        String name,
        String logoUrl) {
    public static BrandResponse fromEntity(Brand brand) {
        return new BrandResponse(brand.getId(), brand.getName(), brand.getLogoUrl());
    }
}

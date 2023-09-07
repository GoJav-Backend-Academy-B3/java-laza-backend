package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private String name;
    private String logoUrl;


    public BrandResponse(Brand brand) {
        this.name = brand.getName();
        this.logoUrl = brand.getLogoUrl();
    }
}

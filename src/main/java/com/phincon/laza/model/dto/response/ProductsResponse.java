package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import lombok.Data;

import java.time.LocalDateTime;

import java.util.List;


@Data
public class ProductsResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer price;
    private LocalDateTime createdAt;
    private CategoryResponse category;
    private List<Size> sizes;

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
        this.sizes = product.getSizes();
    }
}

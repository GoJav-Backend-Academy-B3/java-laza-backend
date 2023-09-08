package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Category;
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
    private Category category;
    private List<Size> sizes;

    public ProductsResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.price = product.getPrice();
        this.createdAt = product.getCreatedAt();
        this.category = product.getCategory();
        this.sizes = product.getSizes();
    }
}

package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String category;
    private List<Product> productList;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.category = category.getCategory();
        this.productList = category.getProductList();
    }


}
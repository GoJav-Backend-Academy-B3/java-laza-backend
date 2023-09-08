package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class CategoryResponse {
    private Long id;
    private String category;
//    private List<ProductsResponse> productList;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.category = category.getCategory();
//        this.productList = category.getProductList().stream()
//                .map(ProductsResponse::new)
//                .collect(Collectors.toList());

    }


}
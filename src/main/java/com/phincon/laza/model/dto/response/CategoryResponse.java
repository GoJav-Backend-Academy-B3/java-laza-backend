package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String category;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.category = category.getCategory();
    }


}

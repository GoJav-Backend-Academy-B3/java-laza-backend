package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
//    Page<Category> getAllCategory(Pageable pageable);
List<Category> getAllCategory();
    Category getCategoryById(Long id);
    Category save( CategoryRequest category);
    Category update(Long id, CategoryRequest category);
    void delete(Long id);




}

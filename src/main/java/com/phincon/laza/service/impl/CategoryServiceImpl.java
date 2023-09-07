package com.phincon.laza.service.impl;


import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.repository.CategoryRepository;
import com.phincon.laza.service.CategoryService;

import com.phincon.laza.validator.CategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category save(CategoryRequest categoryRequest) throws Exception {
        String categoryName = categoryRequest.getCategory();
        categoryValidator.validateCategoryAlreadyExists(categoryName);
        Category category = new Category();
        category.setCategory(categoryName);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) throws Exception {
        Optional<Category> categories = categoryRepository.findById(id);
        categoryValidator.validateCategoryNotFound(categories);
        return categories.get();
    }

    public Category getCategoryByName(String category) throws Exception {
        Optional<Category> categori = categoryRepository.findByCategory(category);
        categoryValidator.validateCategoryNotFound(categori);
        return categori.get();
    }

    public Category update(Long id, CategoryRequest updatedCategory) throws Exception {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        categoryValidator.validateCategoryNotFound(existingCategoryOptional);
        Category existingCategory = existingCategoryOptional.get();
        String updatedCategoryName = updatedCategory.getCategory();
        existingCategory.setCategory(updatedCategoryName);
        return categoryRepository.save(existingCategory);
    }

    public void delete(Long id) throws Exception {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        categoryValidator.validateCategoryNotFound(existingCategoryOptional);
        categoryRepository.deleteById(id);
    }

}
package com.phincon.laza.service.impl;


import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.CategoryRepository;
import com.phincon.laza.service.CategoryService;
import com.phincon.laza.validator.CategoryValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryValidator categoryValidator;
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
    @Override
    public Category save(CategoryRequest categoryRequest) throws Exception {
        String categoryName = categoryRequest.getCategory();
        Optional<Category> existingCategory = categoryRepository.findByCategory(categoryName);
        if (existingCategory.isPresent()) {
            throw new ConflictException("Category already exists");
        }
        Category category = new Category();
        category.setCategory(categoryName);
        return categoryRepository.save(category);
    }
    @Override
    public Category getCategoryById(Long id) throws Exception {
        Optional<Category> categories = categoryRepository.findById(id);
        if (categories.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        return categories.get();
    }

    @Override
    public Category update(Long id, CategoryRequest updatedCategory) throws Exception {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
//        categoryValidator.validateCategoryNotFound(existingCategoryOptional);
        if (existingCategoryOptional.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        Category existingCategory = existingCategoryOptional.get();
        String updatedCategoryName = updatedCategory.getCategory();
        existingCategory.setCategory(updatedCategoryName);
        return categoryRepository.save(existingCategory);
    }
    @Override
    public void delete(Long id) throws Exception {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        if (existingCategoryOptional.isPresent()) {
            Category category = existingCategoryOptional.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return;
        }
        throw new NotFoundException("Category Not Found");

    }
}
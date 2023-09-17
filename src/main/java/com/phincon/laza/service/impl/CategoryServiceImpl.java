package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.repository.CategoryRepository;
import com.phincon.laza.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> categories = categoryRepository.findById(id);
        if (!categories.isPresent()) {
            throw new NotFoundException("Category not found");
        }
        return categories.get();
    }
    @Override
    public Category save(CategoryRequest categoryRequest) {
        String categoryName = categoryRequest.getCategory();
        Optional<Category> existingCategory = categoryRepository.findByCategory(categoryName);
        if (existingCategory.isPresent()) {
            throw new ConflictException("Category already exists");
        }
        Category category = new Category();
        category.setCategory(categoryName);
        return categoryRepository.save(category);
    }
    public Category update(Long id, CategoryRequest updatedCategory) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        if (!existingCategoryOptional.isPresent()) {
            throw new NotFoundException("Category not found");
        }
        Category existingCategory = existingCategoryOptional.get();
        String updatedCategoryName = updatedCategory.getCategory();
        existingCategory.setCategory(updatedCategoryName);
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id){
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

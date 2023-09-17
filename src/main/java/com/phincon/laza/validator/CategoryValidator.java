package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CategoryValidator {
    private final CategoryRepository categoryRepository;
    public void validateCategoryNotFound(Optional<Category> categories) throws Exception {
        if (categories.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
    }
//
//    public void validateCategoryAlreadyExists(String categoryName) throws Exception {
//        Optional<Category> existingCategory = categoryRepository.findByCategory(categoryName);
//        if (existingCategory.isPresent()) {
//            throw new BadRequestException("Category already exists");
//        }
//    }

}

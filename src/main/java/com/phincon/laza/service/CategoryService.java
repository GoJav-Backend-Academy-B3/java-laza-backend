package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.entity.Category;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    List<Category> getAllCategory();
    Category getCategoryById(Long id) throws Exception;
    Category getCategoryByName(String category) throws Exception;
    Category save( CategoryRequest category) throws Exception;
    Category update(Long id, CategoryRequest category) throws  Exception;
    void delete(Long id) throws Exception;

}

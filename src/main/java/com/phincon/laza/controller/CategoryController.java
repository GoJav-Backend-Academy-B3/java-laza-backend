package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.dto.response.CategoryResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.PaginationMeta;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/category")
    public ResponseEntity<DataResponse<List<CategoryResponse>>> getAllCategory() {
        List<Category> categories = categoryService.getAllCategory();
        List<CategoryResponse> categoryResponses = categories.stream().map(CategoryResponse::new).collect(Collectors.toList());
        DataResponse<List<CategoryResponse>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", categoryResponses, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<DataResponse<Category>> getById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);

        DataResponse<Category> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                category,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PostMapping("/management/category")
    public ResponseEntity<DataResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.save(request);
        CategoryResponse result = new CategoryResponse(category);
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>(HttpStatus.CREATED.value(), "Category created successfully", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
    @PutMapping("/management/category/{id}")
    public ResponseEntity<DataResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.update(id, request);
        CategoryResponse result = new CategoryResponse(category);
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Category updated successfully", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @DeleteMapping("/management/category/{id}")
    public ResponseEntity<DataResponse<?>> delete(@PathVariable Long id) {
        categoryService.delete(id);

        DataResponse<Brand> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                null,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }
}
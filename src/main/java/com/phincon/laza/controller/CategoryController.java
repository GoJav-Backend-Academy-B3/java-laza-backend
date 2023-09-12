package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.dto.response.CategoryResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public ResponseEntity<DataResponse<List<CategoryResponse>>> getAllCategory() {
        List<Category> categories = categoryService.getAllCategory();
        List<CategoryResponse> categoryResponses = categories.stream().map(CategoryResponse::new).collect(Collectors.toList());
        DataResponse<List<CategoryResponse>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", categoryResponses, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<CategoryResponse>> getById(@PathVariable Long id) throws Exception {
        Category category = categoryService.getCategoryById(id);
        CategoryResponse result = new CategoryResponse(category);
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/name")
    public ResponseEntity<DataResponse<CategoryResponse>> GetCategoryByName(@RequestParam(name = "category") String categoryName) throws Exception {
        Category category = categoryService.getCategoryByName(categoryName);
        CategoryResponse result = new CategoryResponse(category);
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping
    public ResponseEntity<DataResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) throws Exception {
        Category category = categoryService.save(request);
        CategoryResponse result = new CategoryResponse(category);
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>(HttpStatus.CREATED.value(), "Category created successfully", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) throws Exception {
        Category category = categoryService.update(id, request);
        CategoryResponse result = new CategoryResponse(category);
        DataResponse<CategoryResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Category updated successfully", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<Void>> deleteCategory(@PathVariable Long id) throws Exception {
        categoryService.delete(id);
        DataResponse<Void> dataResponse = new DataResponse<>();
        dataResponse.setStatusCode(HttpStatus.OK.value());
        dataResponse.setMessage("Category deleted successfully");
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}
package com.phincon.laza.service;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.repository.CategoryRepository;
import com.phincon.laza.service.impl.CategoryServiceImpl;
import com.phincon.laza.validator.CategoryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryValidator categoryValidator;

    @BeforeEach
    void setData() {
        List<Category> categoryDataTest = new ArrayList<>();

        categoryDataTest.add(new Category(1L, "Category 1"));
        categoryDataTest.add(new Category(2L, "Category 2"));
        categoryDataTest.add(new Category(3L, "Category 3"));

        lenient().when(categoryRepository.findAll()).thenReturn(categoryDataTest);
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryDataTest.get(0)));
        lenient().when(categoryRepository.findById(4L)).thenReturn(Optional.empty());
    }


    @Test
    @DisplayName("getAllCategory should return a list of categories")
    void getAllCategory() {
        List<Category> result = categoryService.getAllCategory();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Category 1", result.get(0).getCategory());
        assertEquals("Category 2", result.get(1).getCategory());
        assertEquals("Category 3", result.get(2).getCategory());
    }

    @Test
    @DisplayName("getCategoryById should return a category by ID")
    void getCategoryById() {
        Long categoryId = 1L;
        Category expectedCategory = new Category(categoryId, "Category 1");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        Category result = categoryService.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(expectedCategory.getId(), result.getId());
        assertEquals(expectedCategory.getCategory(), result.getCategory());
    }


    @Test
    @DisplayName("getCategoryById should throw NotFoundException for non-existing category")
    void getCategoryByIdNonExisting() {
        Long categoryId = 4L;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    @DisplayName("save should create and return a new category")
    void save() {
        CategoryRequest newCategory = new CategoryRequest("New Category");

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategory(newCategory.getCategory());

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            savedCategory.setId(4L);
            return savedCategory;
        });

        Category savedCategory = categoryService.save(categoryRequest);

        assertNotNull(savedCategory);
        assertEquals(4L, savedCategory.getId());
        assertEquals(categoryRequest.getCategory(), savedCategory.getCategory());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Add category when category Already Exist")
    void whenSaveCategoryThrowException() {
        when(categoryRepository.findByCategory(anyString())).thenReturn(Optional.of(new Category()));
        CategoryRequest categoryRequest = new CategoryRequest("Existing Category");

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            categoryService.save(categoryRequest);
        });

        assertEquals("Category already exists", exception.getMessage());
    }

    @Test
    @DisplayName("update should update and return an existing category")
    void update(){
        Long categoryId = 1L;
        CategoryRequest categoryRequest = new CategoryRequest("Updated Category");

        Category existingCategory = new Category(categoryId, "Category 1");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenAnswer(invocation -> {
            Category updatedCategory = invocation.getArgument(0);
            updatedCategory.setCategory(categoryRequest.getCategory());
            return updatedCategory;
        });

        Category result = categoryService.update(categoryId, categoryRequest);

        assertEquals(categoryId, result.getId());
        assertEquals("Updated Category", result.getCategory());
    }
    @Test
    @DisplayName("update should throw NotFoundException for non-existing category")
    void updateNonExistingCategory() {
        Long categoryId = 4L;
        CategoryRequest categoryRequest = new CategoryRequest("Updated Category");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.update(categoryId, categoryRequest);
        });

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    @DisplayName("delete should delete a category")
    void delete() {
        Long categoryId = 1L;
        assertDoesNotThrow(() -> {
            categoryService.delete(categoryId);
        });


        assertThrows(NotFoundException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });
    }

    @Test
    @DisplayName("delete should throw NotFoundException for non-existing category")
    void deleteNonExisting() {
        Long categoryId = 5L;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.delete(categoryId);
        });

        assertEquals("Category Not Found", exception.getMessage());
    }
}

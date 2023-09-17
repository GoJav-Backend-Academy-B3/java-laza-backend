package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private CategoryController categoryController;

    private SysUserDetails userDetail;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setCategory("TestCategory");

        Category category = new Category();
        category.setId(1L);

        Category category1 = new Category();
        category1.setId(2L);

        List<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category1);

        lenient().when(categoryService.save(request)).thenReturn(category);
        lenient().when(categoryService.getAllCategory()).thenReturn(categories);
        lenient().when(categoryService.getCategoryById(1L)).thenReturn(category);
        lenient().when(categoryService.update(1L, request)).thenReturn(category);
        lenient().doNothing().when(categoryService).delete(1L);

        lenient().when(categoryService.getCategoryById(2L)).thenThrow(new NotFoundException("Category not found"));
        lenient().doThrow(new NotFoundException("Category not found")).when(categoryService).delete(2L);

        userDetail = new SysUserDetails("1", "mawitra", "password",
                Arrays.asList( new SimpleGrantedAuthority("ADMIN")));
    }


    @Test
    @DisplayName("Get all Categories Success")
    public void whenGetAllCategories_thenCorrectResponse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(categoryService, times(1)).getAllCategory();
    }

    @Test
    @DisplayName("Get Category by ID Success")
    public void whenGetCategoryById_thenCorrectResponse() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").exists());

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    @DisplayName("Get Category by ID not found")
    public void whenGetCategoryByIdNotFound_thenCorrectResponse() throws Exception {
        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Category not found"));

        verify(categoryService, times(1)).getCategoryById(2L);
    }

    @Test
    @DisplayName("Add Category Success")
    public void whenAddRequestToCategory_thenCorrectResponse() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setCategory("TestCategory");

        mockMvc.perform(MockMvcRequestBuilders.post("/management/category").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Category created successfully"))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(categoryService, times(1)).save(request);
    }

    @Test
    @DisplayName("Add Category when request is required")
    public void whenAddRequestToCategoryAndRequestIsRequired_thenFailedResponse() throws Exception {
        CategoryRequest request = new CategoryRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/management/category").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.category").value("category is required"));
    }

    @Test
    @DisplayName("Add Category Already Exists")
    public void whenAddCategoryAlreadyExists_thenCorrectResponse() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setCategory("TestCategory");

        when(categoryService.save(request)).thenThrow(new ConflictException("Category already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/management/category").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("Category already exists"));

        verify(categoryService, times(1)).save(request);
    }


    @Test
    @DisplayName("Update Category Success")
    public void whenUpdateCategory_thenCorrectResponse() throws Exception {
        CategoryRequest request = new CategoryRequest();
        Long id = 1L;
        request.setCategory("TestCategory");

        mockMvc.perform(MockMvcRequestBuilders.put("/management/category/{id}", id).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Category updated successfully"))
                .andExpect(jsonPath("$.data.id").exists());

        verify(categoryService, times(1)).update(id, request);
    }

    @Test
    @DisplayName("Update Category Not Found")
    public void whenUpdateCategoryNotFound_thenCorrectResponse() throws Exception {
        CategoryRequest request = new CategoryRequest();
        Long id = 2L;
        request.setCategory("Updated Category");

        when(categoryService.update(id, request)).thenThrow(new NotFoundException("Category not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/management/category/{id}", id).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Category not found"));

        verify(categoryService, times(1)).update(id, request);
    }

    @Test
    @DisplayName("Delete Category Success")
    public void whenDeleteCategory_thenCorrectResponse() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/management/category/{id}", id).with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"));

        verify(categoryService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Delete Category failed")
    public void whenDeleteCategoryAndIdNotFound_thenFailedResponse() throws Exception {
        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/management/category/{id}", id).with(user(userDetail)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Category not found"));

        verify(categoryService, times(1)).delete(2L);
    }
}

package com.phincon.laza.service;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CategoryRequest;
import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.SizeRepository;

import com.phincon.laza.service.impl.SizeServiceImpl;
import com.phincon.laza.validator.SizeValidator;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")

public class SizeServiceTest {

    @Mock
    private SizeRepository sizeRepository;

    @InjectMocks
    private SizeServiceImpl sizeService;

    @Mock
    private SizeValidator sizeValidator;

    @BeforeEach
    void setData() {
        List<Size> sizeDataTest = new ArrayList<>();

        sizeDataTest.add(new Size(1L, "Small"));
        sizeDataTest.add(new Size(2L, "Medium"));
        sizeDataTest.add(new Size(3L, "Large"));

        lenient().when(sizeRepository.findAll()).thenReturn(sizeDataTest);

        lenient().when(sizeRepository.findById(1L)).thenReturn(Optional.of(sizeDataTest.get(0)));
        lenient().when(sizeRepository.findById(4L)).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("getAllSize should return a list of sizes")
    void getAllSize() {
        List<Size> result = sizeService.getAllSize();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Small", result.get(0).getSize());
        assertEquals("Medium", result.get(1).getSize());
        assertEquals("Large", result.get(2).getSize());
    }

    @Test
    @DisplayName("getSizeById should return a size by ID")
    void getSizeById() throws Exception {
        Long sizeId = 1L;
        Size result = sizeService.getSizeById(sizeId);
        assertNotNull(result);
        assertEquals(sizeId, result.getId());
        assertEquals("Small", result.getSize());
    }

    @Test
    @DisplayName("getSizeById should throw NotFoundException for non-existing size")
    void getSizeByIdNonExisting() {
        Long sizeId = 4L;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            sizeService.getSizeById(sizeId);
        });

        assertEquals("Size not found with id: " + sizeId, exception.getMessage());
    }

    @Test
    @DisplayName("save should create and return a new size")
    void save() {
        SizeRequest newSize = new SizeRequest("Extra Large");

        SizeRequest size = new SizeRequest();
        size.setSize(newSize.getSize());

        when(sizeRepository.save(any(Size.class))).thenAnswer(invocation -> {
            Size savedSize = invocation.getArgument(0);
            savedSize.setId(4L);
            return savedSize;
        });

        Size savedSize = sizeService.save(size);

        assertNotNull(savedSize);
        assertEquals(4L, savedSize.getId());
        assertEquals(size.getSize(), savedSize.getSize());


        verify(sizeRepository, times(1)).save(any(Size.class));
    }

    @Test
    @DisplayName("Add size when size Already Exist")
    void whenSaveSizeThrowException() {
        when(sizeRepository.findBySize(anyString())).thenReturn(Optional.of(new Size()));
        SizeRequest sizeRequest = new SizeRequest("Extra Large");

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            sizeService.save(sizeRequest);
        });

        assertEquals("Size already exists", exception.getMessage());
    }

    @Test
    @DisplayName("update should update and return an existing Size")
    void update(){
        Long sizeId = 1L;
        SizeRequest sizeRequest = new SizeRequest("Updated Small");

        Size existingSize= new Size(sizeId, "Small");

        when(sizeRepository.findById(sizeId)).thenReturn(Optional.of(existingSize));
        when(sizeRepository.save(existingSize)).thenAnswer(invocation -> {
            Size updatedSize = invocation.getArgument(0);
            updatedSize.setSize(sizeRequest.getSize());
            return updatedSize;
        });

        Size result = sizeService.update(sizeId, sizeRequest);

        assertEquals(sizeId, result.getId());
        assertEquals("Updated Small", result.getSize());
    }
    @Test
    @DisplayName("update should throw NotFoundException for non-existing Size")
    void updateNonExistingSize() {
        Long sizeId = 4L;
        SizeRequest sizeRequest = new SizeRequest("Updated Category");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            sizeService.update(sizeId, sizeRequest);
        });
        assertEquals("Size not found with id: " + sizeId, exception.getMessage());
    }
    @Test
    @DisplayName("delete should delete a size")
    void delete() {
        Long sizeId = 1L;

        assertDoesNotThrow(() -> {
            sizeService.delete(sizeId);
        });
        }
    @Test
    @DisplayName("delete should throw NotFoundException for non-existing size")
    void deleteNonExisting() {
        Long categoryId = 5L;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            sizeService.delete(categoryId);
        });
        assertEquals("Sizes Not Found", exception.getMessage());
    }
}


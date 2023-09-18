package com.phincon.laza.service;

import com.phincon.laza.model.dto.cloudinary.CloudinaryUploadResult;
import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.repository.BrandRepository;
import com.phincon.laza.service.impl.AddressServiceImpl;
import com.phincon.laza.service.impl.BrandServiceImpl;
import com.phincon.laza.service.impl.CloudinaryImageServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CloudinaryImageServiceImpl cloudinaryImageService;

    @InjectMocks
    private BrandService brandService = new BrandServiceImpl();

    List<Brand> brands = new ArrayList<>();

    @BeforeEach
    public void init() throws Exception {


        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Nike");
        brand.setLogoUrl("https://example.com/logo.png");
        brands.add(brand);

        Brand brand1 = new Brand();
        brand1.setId(2L);
        brand1.setName("Adidas");
        brand1.setLogoUrl("https://example.com/logo.png");
        brands.add(brand1);

        lenient().when(brandRepository.save(any())).thenReturn(brand);
        lenient().when(brandRepository.findAllByIsDeletedFalse(any())).thenReturn(any());
        lenient().when(brandRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(brand));

        lenient().when(cloudinaryImageService.upload(any(), any())).thenReturn(CloudinaryUploadResult.empty());

    }

    @Test
    public void whenAddBrand_thenResponseSuccess() throws Exception {
        BrandRequest request = new BrandRequest();
        request.setName("Nike");
        request.setLogoUrl(new MockMultipartFile("nike",
                InputStream.nullInputStream()));


        Brand brand = brandService.add(request);

        assertEquals("Nike", brand.getName());

        verify(brandRepository, times(1)).save(any());
    }

    @Test
    public void whenGetByIdBrand_thenCorrectResponse() {
        Brand brand = brandService.findById(brands.get(0).getId());
        assertEquals(1L, brand.getId());

        verify(brandRepository, times(1)).findByIdAndIsDeletedFalse(1L);
    }

    @Test
    public void whenUpdateBrand_thenCorrectResponse() throws Exception {
        BrandRequest request = new BrandRequest();
        request.setName("Nike Udated");
        request.setLogoUrl(new MockMultipartFile("nike",
                InputStream.nullInputStream()));

        Brand brand = brandService.update(brands.get(0).getId(), request);
        assertEquals(1L, brand.getId());

        verify(brandRepository, times(1)).save(brand);
    }

    @Test
    public void whenDeleteBrand_thenCorrectResponse() throws Exception {

        brandService.delete(brands.get(0).getId());

        verify(brandRepository, times(1)).save(any());
    }

/*    @Test
    public void whenGetAllBrand_thenCorrectResponse() {
        int page = 0, size = 2;
        Pageable pageRequest = PageRequest.of(page, size);

        Page<Brand> brand = brandService.findAll(pageRequest);

        assertEquals(0, brand.getNumber());
        assertEquals(2, brand.getSize());

        verify(brandRepository, times(1)).findAllByIsDeletedFalse((Pageable) brand);


//        List<Brand> = brandRepository.findAll((Pageable) new PageImpl<>(brands, pageRequest, brands.size()));
    }*/


}

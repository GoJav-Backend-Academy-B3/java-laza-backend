package com.phincon.laza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.phincon.laza.config.BrandDataConfig;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Brand;

@ExtendWith({ MockitoExtension.class })
@Import({ BrandDataConfig.class })
public class BrandServiceTest {

    @Mock
    BrandRepository repository;

    @Mock
    CloudinaryImageService imageService;

    @InjectMocks
    BrandService service;

    @Autowired
    @Qualifier("brand.all")
    private List<Brand> brandAll;

    @Autowired
    @Qualifier("brand.one")
    private Brand brandOne;

    @Autowired
    @Qualifier("brand.one.dup")
    private Brand brandOneDup;

    @Test
    @DisplayName("When find all brand page 1 size 4, return only 2 data returned")
    public void findBrandPage1Size4_2data() {
        Mockito.when(repository.findAll(PageRequest.of(1, 4))).thenReturn(brandAll);

        Page<Brand> brandsPage = service.findAll(1, 4);

        assertEquals(2, brandsPage.getNumberOfElements());
        assertEquals(6, brandsPage.getTotalElements());
        verify(repository, times(1)).findAll(PageRequest.of(1, 4));
    }

    @Test
    @DisplayName("When find one brand, should return data")
    public void findOneBrand_data() {
        Long desiredId = 9l;
        Brand expected = brandAll.get(1);
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.of(expected));

        Brand actual = service.findById(desiredId);

        // I don't know whether this works or not. Just contact me if it doesn't
        // Remove this comment if it works.
        assertEquals(expected, actual);
        verify(repository, times(1)).findById(desiredId);
    }

    @Test
    @DisplayName("When find one brand, should throw exception if not found")
    public void findOneBrand_exception() {
        Long desiredId = 9l;
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(desiredId));

        verify(repository, times(1)).findById(desiredId);
    }

    @Test
    @DisplayName("When add one brand, should return data")
    public void addOneBrand_data() {
        Brand data =  brandOne;
        Mockito.when(repository.save(any(Brand.class))).thenReturn(data);
        Mockito.when(imageService.upload(any(byte[].class), any(String.class), any(String.class))).thenReturn(new CloudinaryUploadResult());

        Brand returned = service.add(data);
        verify(repository, times(1)).save(data);
    }

    @Test
    @DisplayName("When add one brand, should throw exception if name exists")
    public void addOneBrand_exception() {
        Brand data = brandOneDup;
        Mockito.when(repository.save(any(Brand.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ConflictException.class, () -> service.add(data));
        verify(repository, times(1)).save(data);
    }

    @Test
    @DisplayName("When update one brand, should return data")
    public void updateOneBrand_data() {
        long desiredId = 10l;
        Brand data = brandOne;
        Brand updated = new Brand(data.getId(), "lmao", data.getLogoUrl(), data.getProductList());
        Mockito.when(repository.findById(anyLong())).thenReturn(data);
        Mockito.when(repository.save(any(Brand.class))).thenReturn(updated);
        
        service.update(desiredId, updated);

        verify(repository, times(1)).findById(desiredId);
        verify(repository, times(1)).save(updated);
    }

    @Test
    @DisplayName("When update one brand, should throw exception if not found")
    public void updateOneBrand_exception() {
        long desiredId = 10l;
        Brand data = brandOne;
        Brand updated = new Brand(data.getId(), "lmao", data.getLogoUrl(), data.getProductList());

        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () -> service.update(desiredId, updated));

        verify(repository, times(1)).findById(desiredId);
        verify(repository, never()).save(updated);
    }

    @Test
    @DisplayName("When delete one brand, should OK")
    public void deleteOneBrand_OK() {
        long desiredId = 10l;
        Brand data = brandOne;
        Mockito.when(repository.findById(anyLong())).thenReturn(data);

        service.delete(desiredId);

        verify(repository, times(1)).findById(desiredId);
        verify(repository, times(1)).delete(data);
    }

    @Test
    @DisplayName("When delete one brand, should throw exception if not found")
    public void deleteOneBrand_exception() {
        long desiredId = 10l;
        Brand data = brandOne;
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () -> service.delete(desiredId));

        verify(repository, times(1)).findById(desiredId);
        verify(repository, never()).delete(data);
    }
}

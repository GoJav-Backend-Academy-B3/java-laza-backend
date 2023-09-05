package com.phincon.laza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.phincon.laza.config.BrandDataConfig;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Brand;

@ExtendWith({ MockitoExtension.class })
@Import({ BrandDataConfig.class })
public class BrandServiceTest {

    @Mock
    BrandRepository repository;

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
    @DisplayName("When get one brand, should return data")
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
    @DisplayName("When get one brand, should throw exception if not found")
    public void findOneBrand_exception() {
        Long desiredId = 9l;
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(desiredId));

        verify(repository, times(1)).findById(desiredId);
    }
}

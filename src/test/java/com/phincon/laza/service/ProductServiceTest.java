package com.phincon.laza.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.api.exceptions.NotFound;
import com.phincon.laza.config.ProductDataConfig;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.ProductsRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringJUnitConfig(ProductDataConfig.class)
public class ProductServiceTest {
    @Mock
    private ProductsRepository repository;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private SizeService sizeService;

    @Mock
    private CloudinaryImageService cloudinaryImageService;

    @InjectMocks
    private ProductsService service;

    @Autowired
    @Qualifier("product.all")
    private List<Product> products;

    @Autowired
    @Qualifier("product.one")
    private Product productOne;

    @Autowired
    @Qualifier("product.sz.nx")
    private Product productWithSizeNonexistent;

    @Test
    @DisplayName("get all products with page 1 size 4 should return data")
    public void getAllProductPage1Size4_data() {
        List<Product> productSlice = products.subList(4, 6);
        Pageable pageRequest = PageRequest.of(1, 4);
        Mockito.when(repository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(productSlice, pageRequest, productSlice.size()));

        Page<Product> result = service.findAll(1, 4);
        List<Product> actual = result.toList();

        Assertions.assertEquals(1, result.getNumber());
        Assertions.assertEquals(4, result.getSize());
        Assertions.assertEquals(2, result.getNumberOfElements());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(productSlice, actual));
    }

    @Test
    @DisplayName("Find all product containing a keyword should return data")
    public void findAllProductContainingKeyword_data() {
        final String keyword = "helpme";
        final int page = 0, size = 10;
        List<Product> productSlice = products.subList(1, 4);
        Pageable pageRequest = PageRequest.of(page, size);
        Mockito.when(repository.findByNameContaining(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(productSlice, pageRequest, productSlice.size()));

        Page<Product> result = service.findProductByName(keyword, page, size);
        List<Product> actual = result.toList();

        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(3, result.getNumberOfElements());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(productSlice, actual));

        Mockito.verify(repository, Mockito.times(1))
                .findByNameContaining(keyword, pageRequest);
    }

    @Test
    @DisplayName("Get one product with id should return data")
    public void getOneProductId_data() throws Exception {
        final long id = 99l;
        Product expected = productOne;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

        Product actual = service.getProductById(id);

        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("Get one product with id should throw not found exception because not found")
    public void getOneProductId_exception() {
        final long id = 99l;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            service.getProductById(id);
        });

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("Add one product with request should return data")
    public void addOneProduct_data() throws Exception {
        final Brand brand = ProductDataConfig.brandInit().get(2);
        final Category category = ProductDataConfig.categoryInit().get(2);
        final Size sz1 = ProductDataConfig.sizeInit().get(0);
        final Size sz2 = ProductDataConfig.sizeInit().get(1);
        Product product = productOne;
        MultipartFile mockMultipart = new MockMultipartFile("product1image", 
            InputStream.nullInputStream());
        CreateUpdateProductRequest request = new CreateUpdateProductRequest("New product 99", "Product 99 description",
                10, mockMultipart, Arrays.asList(1l, 2l), 1l, 3l);
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);
        Mockito.when(brandService.findById(Mockito.anyLong())).thenReturn(brand);
        Mockito.when(categoryService.getCategoryById(Mockito.anyLong())).thenReturn(category);
        Mockito.when(sizeService.getSizeById(Mockito.anyLong())).thenReturn(sz1, sz2);
        

        service.create(request);
    }
}

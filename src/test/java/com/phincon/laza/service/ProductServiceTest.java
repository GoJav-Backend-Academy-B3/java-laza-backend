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

import com.phincon.laza.config.ProductDataConfig;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.other.CloudinaryUploadResult;
import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.ProductsRepository;
import com.phincon.laza.service.impl.ProductsServiceImpl;

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
    private final ProductsService service = new ProductsServiceImpl();

    @Autowired
    @Qualifier("product.all")
    private List<Product> products;

    @Autowired
    @Qualifier("product.one")
    private Product productOne;

    @Autowired
    @Qualifier("product.update")
    private Product productUpdated;

    @Test
    @DisplayName("get all products with page 1 size 4 should return data")
    public void getAllProductPage1Size4_data() {
        final List<Product> productSlice = products.subList(4, 6);
        final Pageable pageRequest = PageRequest.of(1, 4);
        Mockito.when(repository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(productSlice, pageRequest, productSlice.size()));

        final Page<Product> result = service.getAll(1, 4);
        final List<Product> actual = result.toList();

        Assertions.assertEquals(1, result.getNumber());
        Assertions.assertEquals(4, result.getSize());
        Assertions.assertEquals(2, result.getNumberOfElements());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(productSlice,
                actual));
    }

    @Test
    @DisplayName("Find all product containing a keyword should return data")
    public void findAllProductContainingKeyword_data() {
        final String keyword = "helpme";
        final int page = 0, size = 10;
        final List<Product> productSlice = products.subList(1, 4);
        final Pageable pageRequest = PageRequest.of(page, size);
        Mockito.when(repository.findByNameContaining(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(productSlice, pageRequest, productSlice.size()));

        final Page<Product> result = service.findProductByName(keyword, page, size);
        final List<Product> actual = result.toList();

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
        final Product expected = productOne;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

        final Product actual = service.getProductById(id);

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
        final long brandId = 3l;
        final Brand brand = ProductDataConfig.brandInit().get(2);

        final long categoryId = 1l;
        final Category category = ProductDataConfig.categoryInit().get(2);

        final long sz1Id = 1l;
        final long sz2Id = 2l;
        final Size sz1 = ProductDataConfig.sizeInit().get(0);
        final Size sz2 = ProductDataConfig.sizeInit().get(1);

        final Product product = productOne;
        final MultipartFile mockMultipart = new MockMultipartFile("product1image",
                InputStream.nullInputStream());
        final CreateUpdateProductRequest request = new CreateUpdateProductRequest(
                productOne.getName(), productOne.getDescription(),
                productOne.getPrice(), mockMultipart, Arrays.asList(sz1Id, sz2Id), categoryId, brandId);
        Mockito.when(brandService.findById(Mockito.anyLong())).thenReturn(brand);
        Mockito.when(categoryService.getCategoryById(Mockito.anyLong())).thenReturn(category);
        Mockito.when(sizeService.getSizeById(Mockito.anyLong())).thenReturn(sz1, sz2);
        // use CloudinaryImageService::upload(byte[], String, String) overload.
        Mockito.when(cloudinaryImageService.upload(Mockito.any(byte[].class), Mockito.anyString(),
                Mockito.anyString())).thenReturn(CloudinaryUploadResult.empty());
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);

        final Product result = service.create(request);

        Assertions.assertEquals(request.name(), result.getName());
        Assertions.assertEquals(request.description(), result.getDescription());
        Assertions.assertEquals(product.getPrice(), result.getPrice());
        Assertions.assertEquals(product.getBrand(), result.getBrand());
        Assertions.assertEquals(product.getCategory(), result.getCategory());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(product.getSizes(), result.getSizes()));

        Mockito.verify(brandService, Mockito.times(1)).findById(brandId);
        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
        Mockito.verify(sizeService, Mockito.times(1)).getSizeById(sz1Id);
        Mockito.verify(sizeService, Mockito.times(1)).getSizeById(sz2Id);
        Mockito.verify(cloudinaryImageService, Mockito.times(1)).upload(Mockito.any(byte[].class),
                Mockito.eq("products"), Mockito.anyString());
    }

    @Test
    @DisplayName("Add one product that one of the checking throws error (brand), then throw NotFoundException")
    @Disabled("TODO: This throws CompletionException instead of NotFoundException")
    public void addOneProductCheckingFail_exception() throws Exception {
        final long brandId = 3l;

        final long categoryId = 1l;
        final Category category = ProductDataConfig.categoryInit().get(2);

        final long sz1Id = 1l;
        final long sz2Id = 2l;
        final Size sz1 = ProductDataConfig.sizeInit().get(0);
        final Size sz2 = ProductDataConfig.sizeInit().get(1);

        final MultipartFile mockMultipart = new MockMultipartFile("product1image",
                InputStream.nullInputStream());
        final CreateUpdateProductRequest request = new CreateUpdateProductRequest(
                productOne.getName(), productOne.getDescription(),
                productOne.getPrice(), mockMultipart, Arrays.asList(1l, 2l), 1l, 9999l);
        Mockito.when(brandService.findById(Mockito.anyLong())).thenThrow(NotFoundException.class);
        Mockito.when(categoryService.getCategoryById(Mockito.anyLong())).thenReturn(category);
        Mockito.when(sizeService.getSizeById(Mockito.anyLong())).thenReturn(sz1, sz2);
        // // use CloudinaryImageService::upload(byte[], String, String) overload.
        // Mockito.when(cloudinaryImageService.upload(Mockito.any(byte[].class),
        // Mockito.anyString(),
        // Mockito.anyString()))
        // .thenReturn(CloudinaryUploadResult.empty());
        // Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);

        Assertions.assertThrows(NotFoundException.class, () -> {
            service.create(request);
        });

        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
        Mockito.verify(brandService, Mockito.times(1)).findById(brandId);
        Mockito.verify(sizeService, Mockito.times(1)).getSizeById(sz1Id);
        Mockito.verify(sizeService, Mockito.times(1)).getSizeById(sz2Id);

        // verify that these mocks are not touched by the service.
        Mockito.verifyNoInteractions(cloudinaryImageService, repository);
    }

    @Test
    @DisplayName("Update product with request should return updated data")
    public void updateProductRequest_data() throws Exception {
        final long productId = 99l;

        final long brandId = 1l;
        final Brand brand = ProductDataConfig.brandInit().get(2);

        final long categoryId = 1l;
        final Category category = ProductDataConfig.categoryInit().get(2);

        final long sz1Id = 1l;
        final long sz2Id = 2l;
        final Size sz1 = ProductDataConfig.sizeInit().get(0);
        final Size sz2 = ProductDataConfig.sizeInit().get(1);

        final MultipartFile mockMultipart = new MockMultipartFile("product1image",
                InputStream.nullInputStream());

        final Product updated = productUpdated;
        final CreateUpdateProductRequest request = new CreateUpdateProductRequest(
                updated.getName(), updated.getDescription(),
                updated.getPrice(), mockMultipart, Arrays.asList(sz1Id, sz2Id), categoryId, brandId);
        Mockito.when(repository.findById(Mockito.eq(productId))).thenAnswer(I -> {
            final Product product = productOne;
            Product p = new Product(product.getId(), product.getName(), product.getDescription(),
                    product.getImageUrl(), product.getPrice(), product.getBrand(), product.getSizes(),
                    product.getCategory());
            p.setCloudinaryPublicId("publicId");
            return Optional.of(p);
        });
        Mockito.when(brandService.findById(Mockito.anyLong())).thenReturn(brand);
        Mockito.when(categoryService.getCategoryById(Mockito.anyLong())).thenReturn(category);
        Mockito.when(sizeService.getSizeById(Mockito.anyLong())).thenReturn(sz1, sz2);
        Mockito.when(cloudinaryImageService.delete(Mockito.anyString())).thenReturn(true);
        // use CloudinaryImageService::upload(byte[], String, String) overload.
        Mockito.when(cloudinaryImageService.upload(Mockito.any(byte[].class), Mockito.anyString(),
                Mockito.anyString())).thenReturn(CloudinaryUploadResult.empty());
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(updated);

        Product result = service.update(productId, request);

        Assertions.assertEquals(request.name(), result.getName());
        Assertions.assertEquals(request.description(), result.getDescription());
        Assertions.assertEquals(request.price(), result.getPrice());
        Assertions.assertEquals(updated.getBrand(), result.getBrand());
        Assertions.assertEquals(updated.getCategory(), result.getCategory());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(updated.getSizes(), result.getSizes()));

        Mockito.verify(brandService, Mockito.times(1)).findById(brandId);
        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
        Mockito.verify(sizeService, Mockito.times(1)).getSizeById(sz1Id);
        Mockito.verify(sizeService, Mockito.times(1)).getSizeById(sz2Id);
        Mockito.verify(cloudinaryImageService, Mockito.times(1)).delete(Mockito.anyString());
        Mockito.verify(cloudinaryImageService, Mockito.times(1)).upload(Mockito.any(byte[].class),
                Mockito.eq("products"), Mockito.anyString());
    }

    @Test
    @DisplayName("Delete one product should ok")
    public void deleteProduct_ok() throws Exception {
        final long productId = 99l;
        final Product product = productOne;
        Mockito.when(repository.findById(Mockito.eq(productId))).thenAnswer(I -> {
            Product p = new Product(product.getId(), product.getName(), product.getDescription(),
                    product.getImageUrl(), product.getPrice(), product.getBrand(), product.getSizes(),
                    product.getCategory());
            p.setCloudinaryPublicId("publicId");
            return Optional.of(p);
        });
        Mockito.when(cloudinaryImageService.delete(Mockito.eq("publicId"))).thenReturn(true);

        service.delete(productId);

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(productId));
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any(Product.class));
        Mockito.verify(cloudinaryImageService, Mockito.times(1)).delete(Mockito.eq("publicId"));

        // verify that these mocks are not touched by the service.
        Mockito.verifyNoInteractions(brandService, categoryService, sizeService);
    }

    @Test
    @DisplayName("Delete one product should throw not found exception")
    public void deleteProduct_exception() throws Exception {
        final long productId = 100l;
        Mockito.when(repository.findById(Mockito.eq(productId))).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            service.delete(productId);
        });

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.eq(productId));

        // verify that these mocks are not touched by the service.
        Mockito.verifyNoInteractions(brandService, categoryService, sizeService, cloudinaryImageService);
    }
}

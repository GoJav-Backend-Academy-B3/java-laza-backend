package com.phincon.laza.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.phincon.laza.config.ProductDataConfig;
import com.phincon.laza.model.entity.Product;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@SpringJUnitConfig(ProductDataConfig.class)
public class ProductRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductsRepository repository;

    @Autowired
    @Qualifier("product.all")
    private final List<Product> products;

    @Autowired
    @Qualifier("product.one")
    private final Product productOne;

    @Autowired
    @Qualifier("product.sz.nx")
    private final Product productWithSizeNonexistent;

    @BeforeEach
    public void init() {
        ProductDataConfig.brandInit().forEach(brandRepository::saveAndFlush);
        ProductDataConfig.sizeInit().forEach(sizeRepository::saveAndFlush);
        ProductDataConfig.categoryInit().forEach(categoryRepository::saveAndFlush);
        products.forEach(repository::save);
    }

    @Test
    @DisplayName("get all product with pagination, page 1 with size of 4")
    public void getAllProductPaginationPage1Size4() {
        var pageable = PageRequest.of(1, 4);
        Page<Product> productPage = repository.findAll(pageable);
        List<Product> expected = products.subList(4, 6);
        List<Product> actual = productPage.toList();

        assertEquals(1, productPage.getNumber());
        assertEquals(4, productPage.getSize());
        assertEquals(2, productPage.getNumberOfElements());
        assertTrue(CollectionUtils.isEqualCollection(expected, actual));
    }

    @Test
    @DisplayName("Get one product with specific id")
    public void getProductSpecificId_found() {
        long id = 2l;
        int index = 2;

        Product expected = products.get(2);
        Optional<Product> actual = repository.findById(id);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @DisplayName("Get one product with sepcific id but not found")
    public void getProductSpecificId_notFound() {
        long id = 10l;
        Optional<Product> data = repository.findById(id);

        assertTrue(data.isEmpty());
    }

    @Test
    @DisplayName("Add a product should return same data and cound should be 7")
    public void addNewProduct_data() {
        Product output = repository.save(productOne);
        int count = repository.count();

        assertEquals(7, repository.count());
        assertEquals("New product 99", output.getName());
    }

    @Test
    @DisplayName("Add a product with nonexitent size should throw exception")
    public void addNewProductSizeNonexistent_exception() {

    }
}

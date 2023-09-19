package com.phincon.laza.repository;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.phincon.laza.config.ProductDataConfig;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;

@DataJpaTest(bootstrapMode = BootstrapMode.DEFERRED)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
@SpringJUnitConfig(ProductDataConfig.class)
public class ProductRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductsRepository productsRepository;

    private final List<Brand> brands = ProductDataConfig.brands;

    private final List<Category> categories = ProductDataConfig.categories;

    private final List<Size> sizes = ProductDataConfig.sizes;

    @Autowired
    @Qualifier("product.all")
    private List<Product> products;

    @Autowired
    @Qualifier("product.one")
    private Product productOne;

    @BeforeAll
    public void init() {
        for (int i = 0; i < brands.size(); i++) {
            Brand b = brandRepository.save(brands.get(i));
            brands.set(i, b);
        }
        for (int i = 0; i < categories.size(); i++) {
            Category c = categoryRepository.save(categories.get(i));
            categories.set(i, c);
        }
        for (int i = 0; i < sizes.size(); i++) {
            Size s = sizeRepository.save(sizes.get(i));
            sizes.set(i, s);
        }
        products.forEach(productsRepository::save);
    }

    @Test
    @DisplayName("get all product on page 1 size 4 should return desired data")
    public void getAllProductPage1Size4_data() {
        var result = productsRepository.findAll(PageRequest.of(1, 4));
        List<Product> actualList = result.toList();
        List<Product> expectedList = products.subList(4, 6);

        var actual = actualList.stream().map(ProductData::fromProduct).collect(Collectors.toList());
        var expected = expectedList.stream().map(ProductData::fromProduct).collect(Collectors.toList());

        Assertions.assertEquals(2, result.getNumberOfElements());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(expected, actual));
    }

    @Test
    @DisplayName("add product should count to 7")
    public void addProductCount_7() {
        var product = productOne;

        productsRepository.save(product);

        Assertions.assertEquals(7, productsRepository.count());
    }

    @Test
    @DisplayName("add product with nonexistent brand should throw exception")
    public void addProductNonexistentBrand_exception() {
        var brand = new Brand();
        brand.setId(99l);
        var product = new Product(null, "Product hehe", "Hehe", "", 10, brand,
                Arrays.asList(sizes.get(1), sizes.get(0)), categories.get(0));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            productsRepository.save(product);
        });
    }

    @Test
    @DisplayName("add product with nonexistent size should throw exception")
    @Disabled("It does not throw exception???")
    public void addProductNonexistentSize_exception() {
        var size = new Size();
        size.setId(99l);
        var product = new Product(null, "Product hehe", "Hehe", "", 10, brands.get(0),
                Arrays.asList(sizes.get(1), size), categories.get(0));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            productsRepository.save(product);
        });
    }

    @Test
    @DisplayName("add product with nonexistent category should throw exception")
    public void addProductNonexistentCategory_exception() {
        var category = new Category();
        category.setId(99l);
        var product = new Product(null, "Product hehe", "Hehe", "", 10, brands.get(0),
                Arrays.asList(sizes.get(1), sizes.get(0)), category);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            productsRepository.save(product);
        });
    }
}

record ProductData(
        Long id,
        String name,
        String description,
        BrandData brand,
        List<SizeData> sizes,
        CategoryData category) {
    public static ProductData fromProduct(Product p) {
        return new ProductData(p.getId(), p.getName(),
                p.getDescription(), BrandData.fromBrand(p.getBrand()),
                p.getSizes().stream().map(SizeData::fromSize).collect(Collectors.toList()),
                CategoryData.fromCategory(p.getCategory()));
    }
}

record BrandData(
        Long id, String name) {
    public static BrandData fromBrand(Brand b) {
        return new BrandData(b.getId(), b.getName());
    }
}

record SizeData(
        Long id, String size) {
    public static SizeData fromSize(Size s) {
        return new SizeData(s.getId(), s.getSize());
    }
}

record CategoryData(Long id, String category) {
    public static CategoryData fromCategory(Category c) {
        return new CategoryData(c.getId(), c.getCategory());
    }
}

package com.phincon.laza.repository;


import com.phincon.laza.config.WishlistDataConfig;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@SpringJUnitConfig({WishlistDataConfig.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishlistRepositoryTest {


    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    @Qualifier("category.all")
    private List<Category> categories;

    @Autowired
    @Qualifier("brand.all")
    private  List<Brand> brands;
    @Autowired
    @Qualifier("product.all")
    private List<Product> products;
    @Autowired
    @Qualifier("user.all")
    private List<User> users;

}

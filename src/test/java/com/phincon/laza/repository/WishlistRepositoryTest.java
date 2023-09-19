package com.phincon.laza.repository;


import com.phincon.laza.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishlistRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RoleRepository roleRepository;

    private List<Category> categories = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Brand> brands = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>();
    private List<Cart> carts = new ArrayList<>();
    private Set<Role> roles = new HashSet<>();



    @BeforeEach
    void setUp(){
        Brand brand = new Brand();
        brand.setName("X");
        brand.setLogoUrl("X");
        brand.setIsDeleted(true);
        Brand brandNew = brandRepository.save(brand);
        brands.add(brandNew);

        Category category = new Category();
        category.setCategory("Category");
        category.setIsDeleted(false);
        Category categoryNew = categoryRepository.save(category);
        categories.add(categoryNew);

        Size size = new Size();
        size.setSize("X");
        Size sizeNew = sizeRepository.save(size);
        sizes.add(sizeNew);

        Size sizeI = new Size();
        sizeI.setId(1l);
        sizeI.setSize("XL");
        Size sizeNewI = sizeRepository.save(size);
        sizes.add(sizeNewI);

        Role roleI = new Role();
        roleI.setName(ERole.valueOf("USER"));
        roleRepository.save(roleI);
        Role findRole = roleRepository.findByName(ERole.valueOf("USER")).get();
        roles.add(findRole);


        Product product = new Product();
        product.setId(1l);
        product.setName("Product1");
        product.setDescription("Product1");
        product.setBrand(brandNew);
        product.setImageUrl("Image");
        product.setPrice(10000);
        product.setSizes(sizes);
        product.setCategory(categoryNew);
        Product productNew = productsRepository.save(product);

        Product productI = new Product();
        productI.setId(2l);
        productI.setName("Product2");
        productI.setDescription("Product2");
        productI.setBrand(brandNew);
        productI.setImageUrl("Image");
        productI.setPrice(18000);
        productI.setSizes(sizes);
        productI.setCategory(categoryNew);
        Product productNewI = productsRepository.save(productI);

        products.add(productNewI);
        products.add(productNew);

        User user = new User();
        user.setUsername("user");
        user.setRoles(roles);
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setName("user");
        user.setImageUrl("Image");
        user.setWishlistProducts(products);
        User userNew = userRepository.save(user);
        users.add(userNew);
    }

    @Test
    void whenAddProductToWishlist_thenReturnCorrect(){
        User user = userRepository.findById(users.get(0).getId()).get();
        assertNotNull(user.getWishlistProducts());

        final Integer expectedWishlistNum = 2;
        assertEquals(expectedWishlistNum, user.getWishlistProducts().size());

        Product product = new Product();
        product.setId(3l);
        product.setName("product3");
        product.setDescription("product3");
        product.setImageUrl("image3");
        product.setBrand(brands.get(0));
        product.setImageUrl("Image");
        product.setPrice(18000);
        product.setSizes(sizes);
        product.setCategory(categories.get(0));
        Product newProduct = productsRepository.save(product);
        products.add(newProduct);
        user.setWishlistProducts(products);
        userRepository.save(user);

        final Integer expectedWishlistNumAfterAdd = 3;
        User userResult = userRepository.findById(users.get(0).getId()).get();
        assertEquals(expectedWishlistNumAfterAdd, userResult.getWishlistProducts().size());
        assertEquals(User.class, userResult.getClass());
        assertEquals(Product.class, userResult.getWishlistProducts().get(0).getClass());
    }

    @Test
    void whenDeleteProductFromWishlist_thenReturnCorrect(){
        Optional<User> userOption = userRepository.findById(users.get(0).getId());
        User user = userOption.get();
        assertNotNull(user.getWishlistProducts());

        final Integer expectedWishlistNum = 2;
        assertEquals(expectedWishlistNum, user.getWishlistProducts().size());

        products.remove(0);
        user.setWishlistProducts(products);
        userRepository.save(user);

        final Integer expectedWishlistNumAfterAdd = 1;
        User userResult = userRepository.findById(users.get(0).getId()).get();
        assertEquals(expectedWishlistNumAfterAdd, userResult.getWishlistProducts().size());
        assertEquals(User.class, userResult.getClass());
        assertEquals(Product.class, userResult.getWishlistProducts().get(0).getClass());
    }

    @Test
    void whenAllProductFromWishlist_thenReturnCorrect(){
      Page<Product> productList = productsRepository.findAllByWishlistById(users.get(0).getId(), PageRequest.of(0, 5));
      final int expectedProductWishNum = 2;
      assertNotNull(productList);
      assertEquals(expectedProductWishNum, productList.getContent().size());
      assertEquals(Product.class, productList.getContent().get(0).getClass());
      assertEquals(1, productList.getTotalPages());
      assertEquals(2, productList.getTotalElements());
      assertEquals(products.get(1), productList.getContent().get(0));
    }
}

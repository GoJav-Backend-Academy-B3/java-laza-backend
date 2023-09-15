package com.phincon.laza.repository;

import com.phincon.laza.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
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
    void init(){
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
        roleI.setName(ERole.valueOf("ADMIN"));
        roleRepository.save(roleI);
        Role findRole = roleRepository.findByName(ERole.valueOf("ADMIN")).get();
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
        User userNew = userRepository.save(user);
       users.add(userNew);

        Cart cartI = new Cart();
        cartI.setId(1l);
        cartI.setUser(users.get(0));
        cartI.setProduct(products.get(0));
        cartI.setSize(sizes.get(0));
        cartI.setQuantity(2);
        carts.add(cartRepository.save(cartI));

        cartI.setId(2l);
        cartI.setUser(users.get(0));
        cartI.setQuantity(2);
        cartI.setSize(sizes.get(1));
        carts.add(cartRepository.save(cartI));
    }

    @BeforeEach
    void initEmpty(){
        categories.clear();
        users.clear();
        brands.clear();
        products.clear();
        sizes.clear();
        carts.clear();
        roles.clear();
    }

    @Test
    void testUpdateQuantityById_thenCorrect(){
        User userCheck = userRepository.findByUsername(users.get(0).getUsername()).get();
        List<Cart> cartList = cartRepository.findByUser_Id(userCheck.getId());
        assertEquals(2, cartList.size());
        cartRepository.deleteById(cartList.get(0).getId());
        List<Cart> cartListDelete = cartRepository.findByUser_Id(userCheck.getId());
        assertEquals(1, cartListDelete.size());
    }

    @Test
    void testSave_thenCorrect(){
        Cart newCart = new Cart();
        newCart.setUser(users.get(0));
        newCart.setProduct(products.get(1));
        newCart.setSize(sizes.get(1));
        newCart.setQuantity(2);
        cartRepository.save(newCart);
        List<Cart> cartList = cartRepository.findByUser_Id(users.get(0).getId());
        assertNotNull(cartList);
        assertEquals(3, cartList.size());
        assertEquals(newCart, cartList.get(2));
    }

    @Test
    void testDeleteAllByUser_thenCorrect(){
        List<Cart> cartList = cartRepository.findByUser_Id(users.get(0).getId());
        assertEquals(2, cartList.size());
        cartRepository.deleteByUserId(users.get(0).getId());
        List<Cart> cartListDeleteByUserId = cartRepository.findByUser_Id(users.get(0).getId());
        assertEquals(0, cartListDeleteByUserId.size());
    }

    @Test
    void testDelete_thenCorrect(){
        List<Cart> cartList = cartRepository.findByUser_Id(users.get(0).getId());
        assertEquals(2, cartList.size());
        cartRepository.deleteById(cartList.get(0).getId());
        List<Cart> cartListDeleteByUserId = cartRepository.findByUser_Id(users.get(0).getId());
        assertEquals(1, cartListDeleteByUserId.size());
    }
}

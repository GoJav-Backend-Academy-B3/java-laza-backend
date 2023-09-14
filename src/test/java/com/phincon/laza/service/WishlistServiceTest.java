package com.phincon.laza.service;


import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.ProductsRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.impl.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


@ExtendWith({MockitoExtension.class})
public class WishlistServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private WishlistService wishlistService = new WishlistServiceImpl();

    @BeforeEach
    void init(){
        List<User> userDataTest = new ArrayList<>();
        List<Product> productDataTest = new ArrayList<>();

        userDataTest.add(
                new User("23", "user1", "user1", "password", "email", "image",true, null,null, productDataTest,null,null,null,null,null,null)
        );
        userDataTest.add(
                new User("24", "user2", "user2", "password", "email", "image",true, null,null, productDataTest,null,null,null,null,null,null)
        );

        productDataTest.add(new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),userDataTest,null,null));
        productDataTest.add(new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),userDataTest,null,null));
        productDataTest.add(new Product(92l, "product3", "desc3","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),userDataTest,null,null));
        lenient().when(productsRepository.findAllByWishlistById("23")).thenReturn(productDataTest);
        lenient().when(productsRepository.findAllByWishlistById("no")).thenReturn(new ArrayList<>());

        Product product = productDataTest.get(0);
        product.setWishlistBy(userDataTest);
        Optional<Product> productOptional = Optional.of(product);
        Optional<User> userOptional = Optional.of(userDataTest.get(0));
        Optional<User> userOptionalII = Optional.of(userDataTest.get(1));

        // saveWishlist (there is no wishlist) should return product
        lenient().when(productsRepository.findByIdAndWishlistById(90l, "23")).thenReturn(productOptional);
        lenient().when(userRepository.findById("23")).thenReturn(userOptional);


        // saveWishlist (there is no wishlist) should return product
        lenient().when(productsRepository.findByIdAndWishlistById(90l, "24")).thenReturn(Optional.empty());
        lenient().when(userRepository.findById("24")).thenReturn(userOptionalII);
        lenient().when(productsRepository.findById(90l)).thenReturn(productOptional);

        // saveWishlist (there is no product) should throw NotFoundException
        lenient().when(productsRepository.findByIdAndWishlistById(200l, "23")).thenReturn(Optional.empty());
        lenient().when(userRepository.findById("23")).thenReturn(userOptional);
        lenient().when(productsRepository.findById(200l)).thenReturn(Optional.empty());

    }

    @Test
    @DisplayName("[WishlistService] findWishlistByUser should return products list")
    void whenFindWishlistByUser_thenCorrectResponse(){
        String userId = "23";
        List<Product> productResult = wishlistService.findWishlistByUser(userId);
        assertNotNull(productResult);
        assertEquals(userId, productResult.get(0).getWishlistBy().get(0).getId());
    }

    @Test
    @DisplayName("[WishlistService] findWishlistByUser should return empty product list")
    void whenFindWishlistByUser_thenCorrectResponseEmpty(){
        String userId = "no";
        List<Product> productResult = wishlistService.findWishlistByUser(userId);
        assertEquals(Arrays.asList(), productResult);
    }

    @Test
    @DisplayName("[WishlistService] saveWishlist (there is wishlist) should return product (delete wishlist)")
    void whenSaveWishlist_thenCorrectResponse() throws Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(90l);
        String userId = "23";
        Product productWishlist = wishlistService.createWishlist(userId, wishlistRequest);
        assertNotNull(productWishlist);
        assertEquals(90, productWishlist.getId());
    }

    @Test
    @DisplayName("[WishlistService] saveWishlist (there is no wishlist) should return product (add wishlist)")
    void whenSaveWishlistWithNoWishlist_thenCorrectResponse() throws  Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(90l);
        String userId = "24";
        Product productWishlist = wishlistService.createWishlist(userId, wishlistRequest);
        assertNotNull(productWishlist);
        assertEquals(90, productWishlist.getId());
    }

    @Test
    @DisplayName("[WishlistService] saveWishlist (there is no product) should throw NotFoundException")
    void whenSaveWishlistWithNoProduct_thenCorrectResponse() throws Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(200l);
        String userId = "23";
        assertThrows(NotFoundException.class, ()->{
            wishlistService.createWishlist(userId,wishlistRequest);
        });
    }
}

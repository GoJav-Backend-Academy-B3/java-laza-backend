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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
public class WishlistServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private WishlistService wishlistService = new WishlistServiceImpl();
    private List<User> userDataTest = new ArrayList<>();
    private List<Product> productDataTest = new ArrayList<>();


    @BeforeEach
    void init(){

        List<Product> productListEmpty = new ArrayList<>();

        userDataTest.add(
                new User("23", "user1", "user1", "password", "email", "image",true, null,null, productDataTest,null,null,null,null,null,null)
        );
        userDataTest.add(
                new User("24", "user2", "user2", "password", "email", "image",true, null,null, productDataTest,null,null,null,null,null,null)
        );

        productDataTest.add(new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),userDataTest,null,null));
        productDataTest.add(new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),userDataTest,null,null));
        productDataTest.add(new Product(92l, "product3", "desc3","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),userDataTest,null,null));

        lenient().when(productsRepository.findAllByWishlistById("23", PageRequest.of(0, 5))).thenReturn(new PageImpl<>(productDataTest));
        lenient().when(productsRepository.findAllByWishlistById("no", PageRequest.of(0, 5))).thenReturn(new PageImpl<>(productListEmpty));

        Product product = productDataTest.get(0);
        product.setWishlistBy(userDataTest);
        Optional<Product> productOptional = Optional.of(product);
        Optional<User> userOptional = Optional.of(userDataTest.get(0));
        Optional<User> userOptionalII = Optional.of(userDataTest.get(1));




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
        Page<Product> productResult = wishlistService.findWishlistByUser(userId, 0, 5);
        assertNotNull(productResult);
        assertEquals(1, productResult.getTotalPages());
        assertEquals(PageImpl.class, productResult.getClass());
    }

    @Test
    @DisplayName("[WishlistService] findWishlistByUser should return empty product list")
    void whenFindWishlistByUser_thenCorrectResponseEmpty(){
        String userId = "no";
        Page<Product> productResult = wishlistService.findWishlistByUser(userId, 0,5);
        assertEquals(Arrays.asList(), productResult.getContent());
    }

    @Test
    @DisplayName("[WishlistService] saveWishlist should return correct object")
    void whenSaveWishlist_thenCorrectResponse() throws Exception{
        // saveWishlist (there is no wishlist) should return product
        String userId = "23";
        WishlistRequest requestBody = new WishlistRequest(90l);
        lenient().when(productsRepository.findById(requestBody.getProductId())).thenReturn(Optional.of(productDataTest.get(0)));
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(userDataTest.get(0)));

        Product productWishlist = wishlistService.createWishlist(userId, requestBody);
        assertNotNull(productWishlist);
        assertEquals(Product.class, productWishlist.getClass());
        assertEquals(90, productWishlist.getId());
        assertEquals("product1", productWishlist.getName());
        assertEquals("desc1", productWishlist.getDescription());
    }

    @Test
    @DisplayName("[WishlistService] saveWishlist with invalid product id and throw not found exception")
    void whenSaveWishlist_thenThrowNotFoundException() throws Exception{
        String userId = "23";
        WishlistRequest requestBody = new WishlistRequest(90l);
        lenient().when(productsRepository.findById(requestBody.getProductId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->{
            wishlistService.createWishlist(userId, requestBody);
        });
    }

    @Test
    @DisplayName("[WishlistService] deleteWishlist and does not throw Exception")
    void whenDeleteWishlist_thenCorrectResponse() throws  Exception{
        WishlistRequest requestBody = new WishlistRequest(91l);
        String userId = "24";

        when(productsRepository.findByIdAndWishlistById(requestBody.getProductId(), userId)).thenReturn(Optional.of(productDataTest.get(1)));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDataTest.get(1)));

        assertDoesNotThrow(()->{
            wishlistService.deleteWishlist(userId, requestBody);
        });
    }

    @Test
    @DisplayName("[WishlistService] deleteWishlist and throw NotFoundException when the product wishlist there has no")
    void whenDeleteWishlist_thenThrowException() throws  Exception{
        WishlistRequest requestBody = new WishlistRequest(91l);
        String userId = "24";

        when(productsRepository.findByIdAndWishlistById(requestBody.getProductId(), userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()->{
            wishlistService.deleteWishlist(userId, requestBody);
        });
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

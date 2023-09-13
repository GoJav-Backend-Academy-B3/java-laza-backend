package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.dto.response.WishlistResponse;
import com.phincon.laza.model.entity.*;


import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.WishlistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@AutoConfigureMockMvc
@SpringBootTest
public class WishlistControllerTest {
    private final static String TEST_USER_ID = "23";

    @MockBean
    private WishlistService wService;

    @Autowired WishlistController wishlistController;

    private SysUserDetails userDetail;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockMvc mvc;

    private List<User> users = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>();

    @BeforeEach
    void setData() throws  Exception{
        String userId = "23";
        Long productId = 10l;
        WishlistRequest requestBody = new WishlistRequest(productId);
        WishlistRequest requestBodyProductNotFound = new WishlistRequest(11l);
        Product product = new Product(10l, "Product1", "Product1", "Image Url",100000, null,null, new Brand(10l,"X","logo",false, null),null,null,null,null,null);
        lenient().when(wService.createWishlist(userId, requestBody)).thenReturn(product);
        lenient().when(wService.createWishlist(userId, requestBodyProductNotFound)).thenThrow(new Exception(new NotFoundException("Product not found")));
        userDetail = new SysUserDetails(userId,"smith", "password",
                Arrays.asList(new SimpleGrantedAuthority("USER"),new SimpleGrantedAuthority("ADMIN")));

        this.users.add(new User("23", "user1", "user1", "password", "email", "image", true, null, null, products, null, null, null, null, null, null));
        this.users.add(new User("24", "user2", "user2", "password", "email", "image", true, null, null, products, null, null, null, null, null, null));

        this.sizes.add(new Size(1l, "X", products, false));
        this.sizes.add(new Size(2l, "XL", products, false));
        this.sizes.add(new Size(3l, "XXL", products, false));

        this.products.add(new Product(90l, "product1", "desc1", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));
        this.products.add(new Product(91l, "product2", "desc2", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));
        this.products.add(new Product(92l, "product3", "desc3", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));

    }

    @Test
    @DisplayName("[WishlistControllerTest] Post saveWishlist and should return status OK")
    void postSaveWishlist() throws Exception{

        WishlistResponse wishlistResponse = new WishlistResponse(10l, "Product1", "Image Url", 100000, "X");

        String requestBody = "{\"productId\": 10}";

        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist").with(user(userDetail))
                        .with(csrf())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(wishlistResponse));
    }

    @Test
    @DisplayName("[WishlistControllerTest] Post saveWishlist with invalid product id and should return status not found")
    void postSaveWishlistBadRequest() throws  Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(10l);
//        String requestBody = "{\"productId\": 11}";
        String userId = userDetail.getId();

        when(wService.createWishlist(userId, wishlistRequest)).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist").with(user(userDetail))
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(wishlistRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found"));
    }

    @Test
    @DisplayName("[WishlistControllerTest] Get getProductWishlist and should return status 200")
    void getProductWishlist() throws Exception{
        String userId = userDetail.getId();
        when(wService.findWishlistByUser(userId)).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/wishlist").with(user(userDetail)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.*", hasSize(3)));
    }

}

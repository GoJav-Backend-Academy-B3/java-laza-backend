package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.WishlistResponse;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Product;


import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.WishlistService;

import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.Arrays;

import static org.mockito.Mockito.lenient;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@AutoConfigureMockMvc
@ContextConfiguration(classes = {WishlistController.class})
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class WishlistControllerTest {
    private final static String TEST_USER_ID = "23";

    @MockBean
    private WishlistService wService;

    @Autowired WishlistController wishlistController;

    private SysUserDetails userDetail;
    @Autowired
    private MockMvc mockMvc;

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
    }

    @Test
    @DisplayName("Post saveWishlist and should return status OK")
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

//    @PostMapping("/wishlist")
//    public ResponseEntity<?> saveWishlist(@CurrentUser SysUserDetails ctx, @Valid @RequestBody WishlistRequest wishlistRequest) throws Exception{
//        Product product = wishlistService.createWishlist(ctx.getId(), wishlistRequest);
//        WishlistResponse wishlistResponse = new WishlistResponse(
//                product
//        );
//        DataResponse<WishlistResponse> dataResponse = new DataResponse<>(
//                HttpStatus.OK.value(),
//                HttpStatus.OK.name(),
//                wishlistResponse,
//                null
//        );
//        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
//    }

    @Test
    @DisplayName("Post saveWishlist with invalid product id and should return status not found")
    void postSaveWishlistBadRequest() throws  Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(10l);
//        String requestBody = "{\"productId\": 11}";
        String userId = userDetail.getId();

        when(wService.createWishlist(userId, wishlistRequest)).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist").with(user(userDetail))
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(wishlistRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found"));
    }

}

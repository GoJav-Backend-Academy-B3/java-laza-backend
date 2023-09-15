package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.CustomExceptionHandler;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CartService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {CartController.class})
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class CartControllerTest {
    @MockBean
    private CartService cartService;
    @Autowired CartController cartController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockMvc mvc;

    private SysUserDetails userDetail;
    private List<User> users = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>();
    private List<Cart> cartDataTest = new ArrayList<>();
    private Cart cartOne;


    @BeforeEach
    void setDataMock() throws Exception{
        this.users.add(new User("23", "user1", "user1", "password", "email", "image", true, null, null, null, null, null, null, null, null, null));
        this.users.add(new User("24", "user2", "user2", "password", "email", "image", true, null, null, null, null, null, null, null, null, null));

        this.products.add(new Product(90l, "product1", "desc1", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));
        this.products.add(new Product(91l, "product2", "desc2", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));
        this.products.add(new Product(92l, "product3", "desc3", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));

        this.sizes.add(new Size(1l, "X", products, false));
        this.sizes.add(new Size(2l, "XL", products, false));
        this.sizes.add(new Size(3l, "XXL", products, false));

        this.cartDataTest.add(new Cart(1l, users.get(0), products.get(0), sizes.get(0), 1));
        this.cartDataTest.add(new Cart(2l, users.get(0), products.get(1), sizes.get(1), 2));

        this.cartOne = this.cartDataTest.get(0);
        userDetail = new SysUserDetails(users.get(0).getId(), "smith", "password",
                Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")));


    }

    @AfterTestMethod

    @Test
    @DisplayName("[CartControllerTest] Post saveCart and should return status 200")
    void postSaveCart() throws Exception {
        String userId = users.get(0).getId();
        CartRequest requestBody = new CartRequest( products.get(0).getId(), sizes.get(0).getId());

        lenient().when(cartService.saveCart(userId, requestBody)).thenReturn(cartDataTest.get(0));


        mockMvc.perform(MockMvcRequestBuilders.post("/carts").with(user(userDetail))
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());
        verify(cartService, times(1)).saveCart(userId, requestBody);


    }
    @Test
    @DisplayName("[CartControllerTest] Patch updateCart and should return status 200")
    void patchCart() throws Exception {
        Long cartId = 2l;
        Cart newCart = new Cart(2l, users.get(0), products.get(1), sizes.get(1), 1);

        lenient().when(cartService.updateCart(cartId)).thenReturn(newCart);

        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/{id}", cartId).with(user(userDetail))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());
        verify(cartService, times(1)).updateCart(cartId);
    }

    @Test
    @DisplayName("[CartControllerTest] Post saveCart with bad request and should return status 400")
    void postSaveCartBadRequest() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).setControllerAdvice(CustomExceptionHandler.class)
                .build();

        CartRequest requestBody = new CartRequest( products.get(0).getId(), null);



        mockMvc.perform(MockMvcRequestBuilders.post("/carts").with(user(userDetail))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()));

        mockMvc = mvc ;
    }


    @Test
    @DisplayName("[CartControllerTest] Patch updateCart with invalid id and should return 404")
    void patchCartInvalidId() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).setControllerAdvice(CustomExceptionHandler.class)
                .build();
        Long cartId = 2l;

        lenient().when(cartService.updateCart(2l)).thenThrow(new NotFoundException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/{id}", cartId).with(user(userDetail))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cart not found"));

        verify(cartService, times(1)).updateCart(cartId);
        mockMvc = mvc ;
    }

    @Test
    @DisplayName("[CartControllerTest] delete deleteCartById and should return 200")
    void deleteCart() throws Exception {

        Long cartId = cartDataTest.get(0).getId();
        doNothing().when(cartService).deleteCart(cartId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}", cartId).with(user(userDetail))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

        verify(cartService, times(1)).deleteCart(cartId);
    }

    @Test
    @DisplayName("[CartControllerTest] delete deleteCartById with invalid Id and should return 404")
    void deleteCartInvalidId() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).setControllerAdvice(CustomExceptionHandler.class)
                .build();
        Long cartId = cartDataTest.get(0).getId();
        doThrow(new NotFoundException("Cart not found")).when(cartService).deleteCart(cartId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}", cartId).with(user(userDetail))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cart not found"));

        verify(cartService, times(1)).deleteCart(cartId);
        mockMvc = mvc;
    }

    @Test
    @DisplayName("[CartControllerTest] delete deleteCartByUser and should return 200")
    void deleteCartByUser() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).setControllerAdvice(CustomExceptionHandler.class)
                .build();
        String userId  = users.get(0).getId();
        doNothing().when(cartService).deleteCartByUser(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/all").with(user(userDetail))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()));
        mockMvc = mvc;
    }

@Test
@DisplayName("[CartControllerTest] get findCartByUser and should return 200")
void findCartByUser() throws Exception {

    String userId  = users.get(0).getId();
    when(cartService.findCartByUser(userId)).thenReturn(cartDataTest);
    mockMvc.perform(MockMvcRequestBuilders.get("/carts").with(user(userDetail))
                    .with(csrf()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
}
}

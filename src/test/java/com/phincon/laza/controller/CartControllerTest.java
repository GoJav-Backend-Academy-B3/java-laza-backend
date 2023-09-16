package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.CustomExceptionHandler;
import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CartRequest;

import com.phincon.laza.model.entity.*;

import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CartService;

import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@ContextConfiguration(classes = {CartController.class})
@WebMvcTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CartControllerTest {
    @MockBean
    private CartService cartService;

    @Autowired
    private CartController cartController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private SysUserDetails userDetail;
    private List<User> users = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>();
    private List<Cart> cartDataTest = new ArrayList<>();
    private Cart cartOne;


    @BeforeAll
    void setDataMock() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc  = MockMvcBuilders.standaloneSetup(cartController).setControllerAdvice(new CustomExceptionHandler()).build();
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

    @Test
    @DisplayName("[CartControllerTest] Post saveCart and should return status 200")
    void whenPostSaveCart_thenCorrectResponse() throws Exception {
        CartRequest requestBody = new CartRequest(products.get(0).getId(), sizes.get(0).getId(), 1);

        when(cartService.saveCart(any(), any())).thenReturn(cartDataTest.get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/carts/add").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                      )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());
        verify(cartService, times(1)).saveCart(any(), any());
    }

    @Test
    @DisplayName("[CartControllerTest] Patch updateCart and should return status 200")
    void whenPatchCart_thenReturnCorrectResponse() throws Exception {
        Long productId = products.get(1).getId();
        Long sizeId = sizes.get(1).getId();

        CartRequest requestBody = new CartRequest(productId, sizeId, 2);

        Cart updateCart = new Cart(2l, users.get(0), products.get(1), sizes.get(1), 4);

        when(cartService.updateCart(any(), any())).thenReturn(updateCart);

        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/update").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.productId").value(91));
        verify(cartService, times(1)).updateCart(any(), any());
    }

    @Test
    @DisplayName("[CartControllerTest] Patch updateCart with bad payload and should return status 400")
    void whenPatchCartBadPayload_thenReturnCorrectResponse() throws Exception {

        CartRequest requestBody = new CartRequest(null, null, 2);

        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/update").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sub_error").isNotEmpty());
    }

    @Test
    @DisplayName("[CartControllerTest] Patch updateCart should return status 404")
    void whenPatchCartNotFoundCart_thenReturnCorrectResponse() throws Exception {

        Long productId = products.get(1).getId();
        Long sizeId = sizes.get(1).getId();

        CartRequest requestBody = new CartRequest(productId, sizeId, 2);

        when(cartService.updateCart(any(), any())).thenThrow(new NotFoundException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/update").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cart not found"));
    }

    @Test
    @DisplayName("[CartControllerTest] Patch updateCart should return status 404")
    void whenPatchCartBadQuantity_thenReturnCorrectResponse() throws Exception {

        Long productId = products.get(1).getId();
        Long sizeId = sizes.get(1).getId();

        CartRequest requestBody = new CartRequest(productId, sizeId, 2);

        when(cartService.updateCart(any(), any())).thenThrow(new BadRequestException("The quantity cannot be smaller than the product quantity"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/carts/update").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The quantity cannot be smaller than the product quantity"));
    }

    @Test
    @DisplayName("[CartControllerTest] Post saveCart with bad request and should return status 400")
    void whenPostSaveCartBadRequest_thenReturnCorrectResponse() throws Exception {

        CartRequest requestBody = new CartRequest( products.get(0).getId(), null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/carts/add").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sub_error").isNotEmpty());

    }


    @Test
    @DisplayName("[CartControllerTest] delete deleteCartById and should return 200")
    void whenDeleteCart_thenReturnCorrectResponse() throws Exception {

        Long cartId = cartDataTest.get(0).getId();
        doNothing().when(cartService).deleteCart(cartId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}", cartId).with(user(userDetail)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

        verify(cartService, times(1)).deleteCart(cartId);
    }

    @Test
    @DisplayName("[CartControllerTest] delete deleteCartById with invalid Id and should return 404")
    void whenDeleteCartInvalidId_thenCorrectResponse() throws Exception {
        Long cartId = cartDataTest.get(0).getId();
        doThrow(new NotFoundException("Cart not found")).when(cartService).deleteCart(cartId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}", cartId).with(user(userDetail))
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cart not found"));

        verify(cartService, times(1)).deleteCart(cartId);
    }

    @Test
    @DisplayName("[CartControllerTest] delete deleteCartByUser and should return 200")
    void whenDeleteCartByUser_thenReturnCorrectResponse() throws Exception {
        String userId  = users.get(0).getId();
        doNothing().when(cartService).deleteCartByUser(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/carts/all").with(user(userDetail)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()));
    }

@Test
@DisplayName("[CartControllerTest] get findCartByUser and should return 200")
void whenFindCartByUser_thenReturnCorrectResponse() throws Exception {
    String userId  = users.get(0).getId();
    when(cartService.findCartByUser(userId)).thenReturn(cartDataTest);
    mockMvc.perform(MockMvcRequestBuilders.get("/carts").with(user(userDetail)))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
}
}

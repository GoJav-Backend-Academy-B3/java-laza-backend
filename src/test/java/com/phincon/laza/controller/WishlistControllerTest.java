package com.phincon.laza.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.CustomExceptionHandler;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.WishlistResponse;
import com.phincon.laza.model.entity.*;


import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.WishlistService;

import jakarta.validation.Valid;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;


import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




@ContextConfiguration(classes = WishlistController.class)
@ActiveProfiles("test")
@WebMvcTest
public class WishlistControllerTest {

    @MockBean
     private WishlistService wishlistService;
    @Autowired
     private WishlistController wishlistController  ;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
     private List<User> users = new ArrayList<>();
     private List<Product> products = new ArrayList<>();
     private List<Size> sizes = new ArrayList<>();

     private SysUserDetails userDetail;

    @BeforeEach
    void setup() throws  Exception{
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(wishlistController).setControllerAdvice(new CustomExceptionHandler()).build();


        sizes.add(new Size(1l, "X", null, false));
        sizes.add(new Size(2l, "XL", null, false));
        sizes.add(new Size(3l, "XXL", null, false));

        products.add(new Product(90l, "product1", "desc1", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));
        products.add(new Product(91l, "product2", "desc2", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));
        products.add(new Product(92l, "product3", "desc3", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null));

        users.add(new User("23", "user1", "user1", "password", "email", "image", true, null, null, products, null, null, null, null, null, null));
        users.add(new User("24", "user2", "user2", "password", "email", "image", true, null, null, products, null, null, null, null, null, null));
        userDetail = new SysUserDetails("23", "smith", "password",
                Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")));

    }

    @Test
    @DisplayName("[WishlistControllerTest] Post saveWishlist and should return status OK")
    @WithMockUser
    void whenPostSaveWishlist_thenReturnCorrectResponse() throws Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(90l);
        Product newProduct =new Product(90l, "product1", "desc1", "image", 10000, LocalDateTime.now(), "test", new Brand(10l, "BrandA", "logo", false, null), sizes, new Category(1l, "categoryA", null, false), null, null, null);

        WishlistResponse wishlistResponse = new WishlistResponse(wishlistRequest.getProductId(), "product1", "image", 10000, "BrandA");
        when(wishlistService.createWishlist(any(), any())).thenReturn(newProduct);

        mockMvc.perform(MockMvcRequestBuilders.post("/wishlists/add")
                        .with(user(userDetail))
                        .content(objectMapper.writeValueAsString(wishlistRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(wishlistResponse));

        verify(wishlistService,times(1)).createWishlist(any(),any());
    }

@Test
@DisplayName("[WishlistControllerTest] Post saveWishlist with invalid product id and should return status not found")
    void whenPostSaveWishlistBadRequest_thenReturnCorrectResponse() throws  Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(10l);

        when(wishlistService.createWishlist(any(), any())).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/wishlists/add").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(wishlistRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found"));
    verify(wishlistService,times(1)).createWishlist(any(),any());
    }

    @Test
    @DisplayName("[WishlistControllerTest] Get getProductWishlist and should return status 200")
    void whenGetProductWishlist_thenReturnCorrectResponse() throws Exception{
        when(wishlistService.findWishlistByUser(any())).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/wishlists").with(user(userDetail)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.*", hasSize(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0]").isNotEmpty());

        verify(wishlistService,times(1)).findWishlistByUser(any());

    }

@DisplayName("[WishlistControllerTest] deleteWishlist and should return status 200")
void whenDeleteWishlist_thenReturnCorrectResponse() throws Exception{
    WishlistRequest wishlistRequest = new WishlistRequest(10l);

    doNothing().when(wishlistService).deleteWishlist(any(),any());

    mockMvc.perform(MockMvcRequestBuilders.delete("/wishlists/delete").with(user(userDetail))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(wishlistRequest)))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Successfully delete product wishlist"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(HttpStatus.OK.name()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.OK.value()));
    verify(wishlistService,times(1)).deleteWishlist(any(),any());
}

    @Test
    @DisplayName("[WishlistControllerTest] deleteWishlist and should return status 404")
    void whenDeleteWishlistInvalidId_thenReturnCorrectResponse() throws Exception{
        WishlistRequest wishlistRequest = new WishlistRequest(10l);

        doThrow(new NotFoundException("Product wishlist not found")).when(wishlistService).deleteWishlist(any(),any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/wishlists/delete").with(user(userDetail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wishlistRequest)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product wishlist not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));
        verify(wishlistService,times(1)).deleteWishlist(any(),any());
    }
}

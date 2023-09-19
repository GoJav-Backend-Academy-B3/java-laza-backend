package com.phincon.laza.service;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.CartRepository;
import com.phincon.laza.service.impl.CartServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CartServiceTesting {


    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserService userService;
    @Mock
    private SizeService sizeService;
    @Mock
    private ProductsService productsService;
    @InjectMocks
    private CartService cartService = new CartServiceImpl();

    private Cart cartOne;
     private List<User> users = new ArrayList<>();
     private List<Product> products = new ArrayList<>();
     private List<Size> sizes = new ArrayList<>();
     private List<Cart> cartDataTest = new ArrayList<>();


    @BeforeEach
    void init() throws Exception{

        this.users.add(new User("23", "user1", "user1", "password", "email", "image",true, null,null, null,null,null,null,null,null,null));
        this.users.add(new User("24", "user2", "user2", "password", "email", "image",true, null,null, null,null,null,null,null,null,null));

        this.products.add(new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),sizes,new Category(1l,"categoryA",null,false),null,null,null));
        this.products.add(new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),sizes,new Category(1l,"categoryA",null,false),null,null,null));
        this.products.add(new Product(92l, "product3", "desc3","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),sizes,new Category(1l,"categoryA",null,false),null,null,null));

        this.sizes.add(new Size(1l, "X",products, false));
        this.sizes.add(new Size(2l, "XL", products, false));
        this.sizes.add(new Size(3l, "XXL", products, false));

        this.cartDataTest.add(new Cart(1l, users.get(0), products.get(0),sizes.get(0), 1));
        this.cartDataTest.add(new Cart(2l, users.get(0), products.get(1),sizes.get(1), 2));

        this.cartOne = this.cartDataTest.get(0);
    }

    @AfterEach
    void setEmpty(){
        this.users.clear();
        this.products.clear();
        this.sizes.clear();
        this.cartDataTest.clear();
    }

    @Test
    @DisplayName("[CartService] CreateCart (add an existing product cart) should return cart")
    void whenCreateCart_thenCorrectResponse() throws Exception{
        Cart addCart = cartDataTest.get(0);
        addCart.setQuantity(2);
        Optional<Cart> cartOptional = Optional.of(addCart);

        CartRequest requestBody = new CartRequest(products.get(0).getId(), sizes.get(0).getId(),1);
        lenient().when(cartRepository.findByUserIdAndProductIdAndSizeId(users.get(0).getId(),
                        requestBody.getProductId(), requestBody.getSizeId()))
                .thenReturn(cartOptional);
        lenient().when(cartRepository.save(addCart)).thenReturn(addCart);

        Cart cartResult = cartService.saveCart("23", requestBody);
        assertNotNull(cartResult);
        assertEquals(cartOne, cartResult);
    }

    @Test
    @DisplayName("[CartService] CreateCart (adding products that are not yet in the cart) should return cart")
    void whenCreateCart_thenThrowException() throws Exception{
        CartRequest requestBodyI = new CartRequest(products.get(2).getId(), sizes.get(1).getId(),1);
        lenient().when(cartRepository.findByUserIdAndProductIdAndSizeId(users.get(0).getId(),
                        requestBodyI.getProductId(), requestBodyI.getSizeId()))
                .thenReturn(Optional.empty());
        lenient().when(productsService.getProductById(requestBodyI.getProductId())).thenReturn(products.get(2));
        lenient().when(sizeService.getSizeById(requestBodyI.getSizeId())).thenReturn(sizes.get(1));
        lenient().when(userService.getById("23")).thenReturn(users.get(0));
        Cart newCart = new Cart(any(), users.get(0),products.get(2), sizes.get(1), 1);
        lenient().when(cartRepository.save(newCart)).thenReturn(newCart);

        Cart cartResult = cartService.saveCart("23", requestBodyI);
        assertNotNull(cartResult);
    }

    @Test
    @DisplayName("[CartService] CreateCart (Product not found) should throw Not Found Error")
    void whenCreateCart_thenThrowExceptionProductNotFound() throws Exception{
        CartRequest requestBodyI = new CartRequest(100l, sizes.get(1).getId(),1);
        lenient().when(cartRepository.findByUserIdAndProductIdAndSizeId(users.get(0).getId(),
                        requestBodyI.getProductId(), requestBodyI.getSizeId()))
                .thenReturn(Optional.empty());
        lenient().when(productsService.getProductById(requestBodyI.getProductId()))
                .thenThrow(new NotFoundException("Product not found"));

        assertThrows(NotFoundException.class,()->{
            cartService.saveCart(users.get(0).getId(), requestBodyI);
        });
    }

    @Test
    @DisplayName("[CartService] CreateCart (Size not found) should throw Not Found Error")
    void whenCreateCart_thenThrowExceptionSizeNotFound() throws Exception{
        CartRequest requestBodyI = new CartRequest(products.get(2).getId(), 100l,1);
        lenient().when(sizeService.getSizeById(requestBodyI.getSizeId()))
                .thenThrow(new NotFoundException("Size not found"));

        assertThrows(NotFoundException.class, ()->{
            cartService.saveCart(users.get(0).getId(), requestBodyI);
        });
    }


    @Test
    @DisplayName("[CartService] updateCart")
    void whenUpdateCart_thenReturnCorrectData() throws Exception{
        CartRequest requestBody = new CartRequest(91l, 1l, 1);
        String userId = "23";
        when(cartRepository.findByUserIdAndProductIdAndSizeId(
                userId,
                requestBody.getProductId(),
                requestBody.getSizeId()))
                .thenReturn(Optional.of(cartDataTest.get(1)));

        Cart cart = cartService.updateCart(userId, requestBody);
        assertNotNull(cart);
        assertEquals(1, cart.getQuantity());
        assertEquals("product2", cart.getProduct().getName());
    }


    @Test
    @DisplayName("[CartService] updateCart with invalid request value")
    void whenUpdateCart_thenThrowException() throws Exception{
        CartRequest requestBody = new CartRequest(91l, 1l, 1);
        String userId = "23";
        when(cartRepository.findByUserIdAndProductIdAndSizeId(
                userId,
                requestBody.getProductId(),
                requestBody.getSizeId()))
                .thenReturn(Optional.empty());

       assertThrows(NotFoundException.class, ()->{
           cartService.updateCart(userId, requestBody);
       });
    }

    @Test
    @DisplayName("[CartService] updateCart with quantity more than quantity in cart")
    void whenUpdateCartMoreThanQuantity_thenThrowException() throws Exception{
        CartRequest requestBody = new CartRequest(90l, 1l, 2);
        String userId = "23";
        when(cartRepository.findByUserIdAndProductIdAndSizeId(
                userId,
                requestBody.getProductId(),
                requestBody.getSizeId()))
                .thenReturn(Optional.of(cartDataTest.get(0)));

        assertThrows(BadRequestException.class, ()->{
            cartService.updateCart(userId, requestBody);
        });
    }



    @Test
    @DisplayName("[CartService] updateCart with same quantity")
    void whenUpdateCartWithSameQuantity_thenReturnCorrectData() throws Exception{
        CartRequest requestBody = new CartRequest(90l, 1l, 1);
        String userId = "23";
        when(cartRepository.findByUserIdAndProductIdAndSizeId(userId, requestBody.getProductId(), requestBody.getSizeId())).thenReturn(Optional.of(cartDataTest.get(0)));
        Cart cart = cartService.updateCart("23", requestBody);
        assertNotNull(cart);
        assertEquals(90, cart.getProduct().getId());
        assertEquals(1l, cart.getSize().getId());
        assertEquals("product1", cart.getProduct().getName());
        assertEquals(0, cart.getQuantity());
    }


    @Test
    @DisplayName("[CartService] deleteCart by invalid Cart Id should return throw NotFoundException")
    void whenDeleteCart_thenThrowException(){
        long cartId =999l;
        lenient().when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->{
            cartService.deleteCart(cartId);
        });
    }

    @Test
    @DisplayName("[CartService] deleteCart by Cart Id")
    void whenDeleteCart_thenCorrectResponse() throws Exception{
        long cartId =cartDataTest.get(0).getId();
        lenient().when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartDataTest.get(0)));
        cartService.deleteCart(cartId);
    }

    @Test
    @DisplayName("[CartService] deleteCartByUser by User Id")
    void whenDeleteCartByUserId_thenCorrectResponse() throws Exception{
       String userId = users.get(0).getId();
       doNothing().when(cartRepository).deleteByUserId(userId);
       cartService.deleteCartByUser(userId);
    }

    @Test
    @DisplayName("[CartService] findCartByUser by User Id")
    void whenFindCartByUser_thenCorrectResponse() throws Exception{
        String userId = users.get(0).getId();
        lenient().when(cartRepository.findByUser_Id(userId)).thenReturn(cartDataTest);
        cartService.findCartByUser(userId);
    }
}

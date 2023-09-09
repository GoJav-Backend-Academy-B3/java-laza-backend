package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.dto.response.CartResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<DataResponse<CartResponse>> saveCart(@CurrentUser SysUserDetails ctx, @Valid @RequestBody CartRequest request)throws  Exception{
        Cart cart = cartService.saveCart(ctx.getId(),request);
        CartResponse cartResponse = new CartResponse(cart);
        DataResponse<CartResponse> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                cartResponse,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/carts/{id}")
    public ResponseEntity<DataResponse<CartResponse>> updateCart(@PathVariable(value = "id") long id) throws  Exception{
        Cart cart = cartService.updateCart(id);
        CartResponse cartResponse = new CartResponse(cart);
        DataResponse<CartResponse> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                cartResponse,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<DataResponse<String>> deleteCartById(@PathVariable(value = "id") long id) throws Exception{
        cartService.deleteCart(id);
        DataResponse<String> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "Successfully delete cart",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/carts")
    public ResponseEntity<DataResponse<String>> deleteCartByUser(@CurrentUser SysUserDetails ctx){
        cartService.deleteCartByUser(ctx.getId());
        DataResponse<String> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "Successfully delete carts",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/carts")
    public ResponseEntity<DataResponse<List<CartResponse>>> findCartByUser(@CurrentUser SysUserDetails ctx){
        List<Cart> carts = cartService.findCartByUser(ctx.getId());

        List<CartResponse> cartResponses = new ArrayList<>();
        for (Cart cart: carts){
            CartResponse cartResponse = new CartResponse(
                    cart
            );
            cartResponses.add(cartResponse);
        }

        DataResponse<List<CartResponse>> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                cartResponses,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

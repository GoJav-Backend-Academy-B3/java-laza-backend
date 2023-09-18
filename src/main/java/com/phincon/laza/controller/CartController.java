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

    @PostMapping("/carts/add")
    public ResponseEntity<DataResponse<CartResponse>> saveCart(@CurrentUser SysUserDetails ctx, @Valid @RequestBody CartRequest request)throws  Exception{
        Cart cart = cartService.saveCart(ctx.getId(),request);
        CartResponse cartResponse = new CartResponse(cart);
        return DataResponse.ok(cartResponse);
    }

    @PatchMapping("/carts/update")
    public ResponseEntity<DataResponse<CartResponse>> updateCart(@CurrentUser SysUserDetails ctx, @Valid @RequestBody  CartRequest request) throws  Exception{
        Cart cart = cartService.updateCart(ctx.getId(), request);
        CartResponse cartResponse = new CartResponse(cart);
        return DataResponse.ok(cartResponse);
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<DataResponse<String>> deleteCartById(@PathVariable(value = "id") long id) throws Exception{
        cartService.deleteCart(id);
        return DataResponse.ok("Successfully delete cart");
    }

    @DeleteMapping("/carts/all")
    public ResponseEntity<DataResponse<String>> deleteCartByUser(@CurrentUser SysUserDetails ctx){
        cartService.deleteCartByUser(ctx.getId());
        return DataResponse.ok("Successfully delete carts");
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

        return DataResponse.ok(cartResponses);
    }


}

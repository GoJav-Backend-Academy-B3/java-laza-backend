package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.service.CartService;
import jakarta.validation.Valid;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<DataResponse<Cart>> saveCart(@AuthenticationPrincipal UserDetails ctx, @Valid @RequestBody CartRequest request)throws  Exception{
        Cart cart = cartService.saveCart(ctx.getUsername(),request);
        DataResponse<Cart> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                cart,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

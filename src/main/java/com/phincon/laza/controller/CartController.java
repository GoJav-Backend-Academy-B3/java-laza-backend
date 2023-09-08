package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;

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

    @PatchMapping("/carts/{id}")
    public ResponseEntity<DataResponse<Cart>> updateCart(@PathVariable(value = "id") long id) throws  Exception{
        Cart cart = cartService.updateCart(id);
        DataResponse<Cart> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                cart,
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
    public ResponseEntity<DataResponse<String>> deleteCartByUser(@AuthenticationPrincipal UserDetails ctx){
        cartService.deleteCartByUser(ctx.getUsername());
        DataResponse<String> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "Successfully delete carts",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/carts")
    public ResponseEntity<DataResponse<List<Cart>>> findCartByUser(@AuthenticationPrincipal UserDetails ctx){
        List<Cart> carts = cartService.findCartByUser(ctx.getUsername());
        DataResponse<List<Cart>> response = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                carts,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

package com.phincon.laza.controller;


import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.WishlistResponse;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("WishlistController")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/wishlist")
    public ResponseEntity<?> saveWishlist(@CurrentUser SysUserDetails ctx, @Valid @RequestBody WishlistRequest wishlistRequest) throws Exception{
        Product product = wishlistService.createWishlist(ctx.getId(), wishlistRequest);
        WishlistResponse wishlistResponse = new WishlistResponse(
                product
        );
        DataResponse<WishlistResponse> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                wishlistResponse,
                null
        );
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getProductWishlist(@CurrentUser SysUserDetails ctx)
    {
        List<Product> products = wishlistService.findWishlistByUser(ctx.getId());
        List<WishlistResponse> wishlistResponses = new ArrayList<>();
        for (Product product: products){
            wishlistResponses.add(new WishlistResponse(product));
        }
        DataResponse<List<WishlistResponse>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                wishlistResponses,
                null
        );
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}

package com.phincon.laza.controller;


import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.WishlistResponse;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    @Autowired
    private final WishlistService wishlistService;


    @PostMapping("/wishlists/add")
    public ResponseEntity<?> saveWishlist(@CurrentUser SysUserDetails ctx, @Valid @RequestBody WishlistRequest wishlistRequest) throws Exception{
        Product product = wishlistService.createWishlist(ctx.getId(), wishlistRequest);
        WishlistResponse wishlistResponse = new WishlistResponse(product);
        return DataResponse.ok(wishlistResponse);
    }

    @DeleteMapping("/wishlists/delete")
    public  ResponseEntity<?> deleteWishlist(@CurrentUser SysUserDetails ctx, @Valid @RequestBody WishlistRequest wishlistRequest) throws Exception{
        wishlistService.deleteWishlist(ctx.getId(), wishlistRequest);
        return DataResponse.ok("Successfully delete product wishlist");
    }

    @GetMapping("/wishlists")
    public ResponseEntity<?> getProductWishlist(@CurrentUser SysUserDetails ctx)
    {
        List<Product> products = wishlistService.findWishlistByUser(ctx.getId());
        List<WishlistResponse> wishlistResponses = new ArrayList<>();
        for (Product product: products){
            wishlistResponses.add(new WishlistResponse(product));
        }
        return DataResponse.ok(wishlistResponses);
    }
}

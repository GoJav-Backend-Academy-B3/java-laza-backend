package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface WishlistService {
    Product createWishlist(String userId,WishlistRequest wishlistRequest) throws Exception;
    List<Product> findWishlistByUser(String userId) ;

    void deleteWishlist(String userId, WishlistRequest wishlistRequest) throws Exception;
}

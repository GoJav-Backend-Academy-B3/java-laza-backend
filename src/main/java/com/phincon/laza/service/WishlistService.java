package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.entity.Product;

import java.util.List;

public interface WishlistService {
    public Product createWishlist(String userId,WishlistRequest wishlistRequest) throws Exception;
    public List<Product> findWishlistByUser(String userId) ;
}

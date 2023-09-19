package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface WishlistService {
    Product createWishlist(String userId,WishlistRequest wishlistRequest) throws Exception;
    Page<Product> findWishlistByUser(String userId, int page, int size) ;

    void deleteWishlist(String userId, WishlistRequest wishlistRequest) throws Exception;
}

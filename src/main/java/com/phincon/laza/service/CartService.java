package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.model.entity.Product;

import java.util.List;

public interface CartService {
     Cart saveCart(String userId,CartRequest cartRequest) throws Exception;
     Cart updateCart(String userId, CartRequest cartRequest) throws Exception;
     void deleteCart(Long cartId) throws Exception;
     void deleteCartByUser(String userId);
     List<Cart> findCartByUser(String userId);

     List<Product> findAllProductInCartByUser(String userId);
}

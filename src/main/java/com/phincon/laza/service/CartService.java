package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.model.entity.Product;

import java.util.List;

public interface CartService {
    public Cart saveCart(String userId,CartRequest cartRequest) throws Exception;
    public Cart updateCart(Long cartId) throws Exception;
    public void deleteCart(Long cartId) throws Exception;
    public void deleteCartByUser(String userId);
    public List<Cart> findCartByUser(String userId);

    public List<Product> findAllProductInCartByUser(String userId);
}

package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {
    public Cart saveCart(String userId,CartRequest cartRequest) throws Exception;
    public Cart updateCart(Long cartId) throws Exception;
    public void deleteCart(Long cartId) throws Exception;
    public void deleteCartByUser(String userId);
    public List<Cart> findCartByUser(String userId);
}

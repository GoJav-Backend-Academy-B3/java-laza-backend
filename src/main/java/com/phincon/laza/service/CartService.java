package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;

import java.util.List;

public interface CartService {
    public Cart saveCart(String userName,CartRequest cartRequest) throws Exception;
    public Cart updateCart(Long cartId) throws Exception;
    public void deleteCart(Long cartId) throws Exception;
    public void deleteCartByUser(String username);
    public List<Cart> findCartByUser(String username);
}

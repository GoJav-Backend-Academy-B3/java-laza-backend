package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;

public interface CartService {
    public Cart saveCart(String userName,CartRequest cartRequest) throws Exception;
}

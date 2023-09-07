package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.CartRepository;
import com.phincon.laza.service.CartService;
import com.phincon.laza.service.ProductsService;
import com.phincon.laza.service.SizeService;
import com.phincon.laza.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private UserService userService;
    private SizeService sizeService;
    private ProductsService productsService;

    @Override
    public Cart saveCart(String userName,CartRequest cartRequest) throws Exception{
        Product product = productsService.getProductById(cartRequest.getProductId());
        Size size = sizeService.getSizeById(cartRequest.getSizeId());
        User user = userService.getByUsername(userName);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setSize(size);
        return cartRepository.save(cart);
    }
}

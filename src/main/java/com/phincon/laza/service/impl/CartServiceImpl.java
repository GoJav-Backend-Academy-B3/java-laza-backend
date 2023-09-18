package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private ProductsService productsService;

    @Override
    public Cart saveCart(String userId,CartRequest cartRequest) throws Exception{

        Optional<Cart> cartProduct = cartRepository.findByUserIdAndProductIdAndSizeId(userId, cartRequest.getProductId(), cartRequest.getSizeId());

        if (cartProduct.isPresent()){
            Cart cart = cartProduct.get();
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
            return cartRepository.save(cart);
        }

        Product product = productsService.getProductById(cartRequest.getProductId());
        Size size = sizeService.getSizeById(cartRequest.getSizeId());
        User user = userService.getById(userId);

        return cartRepository.save(new Cart(0l, user, product, size, cartRequest.getQuantity()));
    }

    @Override
    public Cart updateCart(String userId, CartRequest cartRequest) throws Exception {

        Optional<Cart> cartProduct = cartRepository.findByUserIdAndProductIdAndSizeId(userId, cartRequest.getProductId(), cartRequest.getSizeId());

        if (cartProduct.isEmpty()){
            throw new NotFoundException("Cart not found");
        }
        if (cartRequest.getQuantity() > cartProduct.get().getQuantity()){
            throw new BadRequestException("The quantity cannot be smaller than the product quantity");
        }

        if (cartRequest.getQuantity().equals(cartProduct.get().getQuantity())){
            cartRepository.deleteById(cartProduct.get().getId());
            cartProduct.get().setQuantity(0);
            return cartProduct.get();
        }

        cartProduct.get().setQuantity(cartProduct.get().getQuantity() - cartRequest.getQuantity());
        cartRepository.save(cartProduct.get());
        return cartProduct.get();
    }

    @Override
    public void deleteCart(Long cartId) throws Exception {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()){
            throw new NotFoundException("Cart not found");
        }
        cartRepository.deleteById(cartId);
    }

    @Override
    public void deleteCartByUser(String userId) {
        cartRepository.deleteByUserId(userId);
    }

    @Override
    public List<Cart> findCartByUser(String userId) {
        List<Cart> carts = cartRepository.findByUser_Id(userId);
        return carts;
    }

    @Override
    public List<Product> findAllProductInCartByUser(String userId) {
        List<Cart> carts = cartRepository.findByUser_Id(userId);
        List<Product> products = new ArrayList<>();
        for (Cart cart : carts ) {
            products.add(cart.getProduct());
        }

        return products;
    }
}

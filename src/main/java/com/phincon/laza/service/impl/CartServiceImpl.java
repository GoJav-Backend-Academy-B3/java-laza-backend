package com.phincon.laza.service.impl;

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
        Product product = productsService.getProductById(cartRequest.getProductId());
        Size size = sizeService.getSizeById(cartRequest.getSizeId());
        User user = userService.getById(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setSize(size);

        Cart cartFind = cartRepository.findByUserIdAndProductIdAndSizeId(
                cart.getUser().getId(),
                cartRequest.getProductId(),
                cartRequest.getSizeId());

        cart.setId((cartFind==null) ? 0 : cartFind.getId());
        cart.setQuantity((cartFind==null ? 0 : cartFind.getQuantity()) +1);
        Cart result = cartRepository.save(cart);
        return cartRepository.findById(result.getId()).get();
    }

    @Override
    public Cart updateCart(Long cartId) throws Exception {

        Optional<Cart> getCart = cartRepository.findById(cartId);

        if (!getCart.isPresent()){
            throw new NotFoundException("Cart not found");
        }

        if (getCart.get().getQuantity() == 1){
            cartRepository.deleteById(getCart.get().getId());
            getCart.get().setQuantity(0);
            return getCart.get();
        }

        cartRepository.updateQuantityById(getCart.get().getQuantity()-1, getCart.get().getId());
        Cart result = cartRepository.findById(getCart.get().getId()).get();
        result.setQuantity(result.getQuantity()-1);
        return result;
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
}

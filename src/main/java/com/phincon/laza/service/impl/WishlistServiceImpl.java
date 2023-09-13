package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.WishlistRequest;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.ProductsRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.ProductsService;
import com.phincon.laza.service.UserService;
import com.phincon.laza.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public Product createWishlist(String userId,WishlistRequest wishlistRequest) throws Exception {
        Optional<Product> product1Wishlist = productsRepository.findByIdAndWishlistById(wishlistRequest.getProductId(),userId);
        Optional<User> user = userRepository.findById(userId);
        User userWishlist = user.get();

        if (product1Wishlist.isPresent()){
            userWishlist.removeProductWishlist(wishlistRequest.getProductId());
            userRepository.save(userWishlist);
            return product1Wishlist.get();
        }

        Optional<Product> product = productsRepository.findById(wishlistRequest.getProductId());
        if (product.isEmpty()){
            throw new NotFoundException("Product not found");
        }
        userWishlist.addWishlist(product.get());
        userRepository.save(userWishlist);
        return product.get();
    }

    @Override
    public List<Product> findWishlistByUser(String userId) {
        return productsRepository.findAllByWishlistById(userId);
    }
}

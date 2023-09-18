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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public Product createWishlist(String userId,WishlistRequest wishlistRequest) throws Exception {

        Optional<Product> product = productsRepository.findById(wishlistRequest.getProductId());
        if (product.isEmpty()){
            throw new NotFoundException("Product not found");
        }

        Optional<User> user = userRepository.findById(userId);
        user.get().addWishlist(product.get());
        userRepository.save(user.get());
        return product.get();
    }

    @Override
    public void deleteWishlist(String userId, WishlistRequest wishlistRequest) throws Exception{
        Optional<Product> product = productsRepository.findByIdAndWishlistById(wishlistRequest.getProductId(), userId);

        if (product.isEmpty()){
            throw new NotFoundException("Product wishlist not found");
        }

        Optional<User> userWishlist = userRepository.findById(userId);
        User user = userWishlist.get();
        user.removeProductWishlist(wishlistRequest.getProductId());
        userRepository.save(user);
    }

    @Override
    public List<Product> findWishlistByUser(String userId) {
        return productsRepository.findAllByWishlistById(userId);
    }
}

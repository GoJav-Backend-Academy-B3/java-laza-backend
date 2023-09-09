package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndWishlistById(Long productId,String userId);
    List<Product> findAllByWishlistById(String userId);
}

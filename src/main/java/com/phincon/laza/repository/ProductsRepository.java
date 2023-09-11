package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContaining(String keyword, Pageable page);
    Optional<Product> findByIdAndWishlistById(Long productId, String userId);
    List<Product> findAllByWishlistById(String userId);

}

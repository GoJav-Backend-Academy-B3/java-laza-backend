package com.phincon.laza.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phincon.laza.model.entity.Product;


@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.isDeleted=false")
    Page<Product> findAll(Pageable page);

    @Query("SELECT p FROM Product p WHERE p.isDeleted=false AND LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Product> findByNameContaining(String keyword, Pageable page);

    Optional<Product> findByIdAndWishlistById(Long productId, String userId);
    Page<Product> findAllByWishlistById(String userId, Pageable pageable);
}

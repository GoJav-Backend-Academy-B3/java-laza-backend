package com.phincon.laza.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phincon.laza.model.entity.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContaining(String keyword, Pageable page);
}

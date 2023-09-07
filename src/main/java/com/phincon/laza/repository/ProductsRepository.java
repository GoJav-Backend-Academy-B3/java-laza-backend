package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

//    List<Product> findTop2ById ()
}

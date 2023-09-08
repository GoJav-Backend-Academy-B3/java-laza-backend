package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Cart;
import com.phincon.laza.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserIdAndProductIdAndSizeId(String userId, Long productId, Long sizeId);

    @Modifying
    @Query("UPDATE Cart c SET c.quantity = ?1 WHERE c.id = ?2")
    void updateQuantityById(Integer quantity, Long id);
    void deleteByUserId(String userId);
    List<Cart> findByUserId(String userId);
}

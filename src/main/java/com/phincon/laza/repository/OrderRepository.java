package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.user = :user ORDER BY o.createdAt DESC")
    Page<Order> findAllByUserIdOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);
}

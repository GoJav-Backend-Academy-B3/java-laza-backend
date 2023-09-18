package com.phincon.laza.repository;

import com.phincon.laza.model.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findAllByIsActiveIsTrue();
    Optional<PaymentMethod> findTop1ByNameAndIsActiveIsTrue(String name);
}


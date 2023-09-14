package com.phincon.laza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phincon.laza.model.entity.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

    List<CreditCard> findAllByUserId(String userId);
}

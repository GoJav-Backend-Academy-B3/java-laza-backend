package com.phincon.laza.repository;

import com.phincon.laza.model.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, String> {
}


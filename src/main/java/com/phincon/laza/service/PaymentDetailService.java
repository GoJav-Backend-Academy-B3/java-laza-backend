package com.phincon.laza.service;

import com.phincon.laza.model.entity.PaymentDetail;

import java.util.List;

public interface PaymentDetailService {

    List<PaymentDetail> getAllPaymentDetails();

    PaymentDetail getPaymentDetailById(String id);

    PaymentDetail createPaymentDetail(PaymentDetail paymentDetail);
}


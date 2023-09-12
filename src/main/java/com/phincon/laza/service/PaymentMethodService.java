package com.phincon.laza.service;

import com.phincon.laza.model.entity.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethod> getAllPaymentMethods();

    List<PaymentMethod> getAllActivePaymentMethods();

    PaymentMethod getPaymentMethodById(Long id);

    PaymentMethod createPaymentMethod(PaymentMethod paymentMethod);

    PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedPaymentMethod);

    void deletePaymentMethod(Long id);

    PaymentMethod deactivatePaymentMethod(Long id);

    PaymentMethod activatePaymentMethod(Long id);
}


package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentMethodValidator {

    public void validatePaymentMethodNotFound(Optional<PaymentMethod> paymentMethod, Long id) {
        if (paymentMethod.isEmpty()) {
            throw new NotFoundException(String.format("payment method with id %s not found", id));
        }
    }
}

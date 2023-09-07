package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.PaymentDetail;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentDetailValidator {
    public void validatePaymentDetailNotFound(Optional<PaymentDetail> paymentDetail, String id) {
        if (paymentDetail.isEmpty()) {
            throw new NotFoundException(String.format("payment detail with id %s not found", id));
        }
    }
}

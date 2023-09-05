package com.phincon.laza.exception.custom;

import com.phincon.laza.model.entity.PaymentMethod;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

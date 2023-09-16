package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderValidator {
    public void validateOrderNotFound(Optional<Order> order, String id) {
        if (order.isEmpty()) {
            throw new NotFoundException(String.format("order with id %s not found", id));
        }
    }
}

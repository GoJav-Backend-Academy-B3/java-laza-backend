package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor

public class ReviewValidator {
    private static OrderRepository orderRepository;
    public static void validateOrderAlreadyExists(String orderId) throws Exception {
        Optional<Order> existingCategory = orderRepository.findById(orderId);
        if (existingCategory.isPresent()) {
            throw new BadRequestException("Order_id already exists");
        }
    }
}

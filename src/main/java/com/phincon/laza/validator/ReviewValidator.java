package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ReviewValidator {
    private final OrderRepository orderRepository;

}

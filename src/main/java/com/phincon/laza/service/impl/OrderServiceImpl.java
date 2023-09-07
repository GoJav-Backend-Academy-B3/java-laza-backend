package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.repository.OrderRepository;
import com.phincon.laza.service.OrderService;
import com.phincon.laza.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final OrderValidator orderValidator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(String id) {
        Optional<Order> order = orderRepository.findById(id);

        orderValidator.validateOrderNotFound(order, id);

        return order.get();
    }

    @Override
    public Order createOrder(Order order) {
        try {
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public Order updateOrder(String id, Order updatedOrder) {
        Optional<Order> order = orderRepository.findById(id);

        orderValidator.validateOrderNotFound(order, id);

        updatedOrder.setId(id);
        return orderRepository.save(updatedOrder);
    }

    @Override
    public void deleteOrder(String id) {
        Optional<Order> order = orderRepository.findById(id);

        orderValidator.validateOrderNotFound(order, id);

        orderRepository.deleteById(id);
    }


}

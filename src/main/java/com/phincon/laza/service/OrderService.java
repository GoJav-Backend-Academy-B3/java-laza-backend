package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.CheckoutRequest;
import com.phincon.laza.model.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(String id);
    Order createOrder(Order order);

    Order requestCreateOrder(String username, CheckoutRequest checkoutRequest);

    Order updateOrder(String id, Order updatedOrder);

    void deleteOrder(String id);
}


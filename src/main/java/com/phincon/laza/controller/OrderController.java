package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CheckoutRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<DataResponse<Order>> checkout(@CurrentUser SysUserDetails ctx, @Valid @RequestBody CheckoutRequest checkoutRequest) {
        Order order = orderService.requestCreateOrder(ctx.getId(), checkoutRequest);

        return DataResponse.ok(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<DataResponse<Order>> getPaymentMethodById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        return DataResponse.ok(order);
    }
}

package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CheckoutRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<DataResponse<Order>> checkout(@AuthenticationPrincipal UserDetails ctx, @Valid @RequestBody CheckoutRequest checkoutRequest) {
        Order order = orderService.requestCreateOrder(ctx.getUsername(), checkoutRequest);

        return DataResponse.ok(order);
    }
}

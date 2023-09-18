package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.CheckoutRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.PaginationMeta;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<DataResponse<Order>> checkout(@CurrentUser SysUserDetails ctx, @Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {
        Order order = orderService.requestCreateOrder(ctx.getId(), checkoutRequest);

        return DataResponse.ok(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<DataResponse<Order>> getOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        return DataResponse.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<DataResponse<List<Order>>> getAllOrderByUserId(@CurrentUser SysUserDetails ctx, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> orderPage = orderService.getAllOrdersByUserIdPage(page, size, ctx.getId());

        PaginationMeta meta = new PaginationMeta(page, size, orderPage.getNumberOfElements());
        var data = orderPage.get()
                .collect(Collectors.toList());

        return DataResponse.ok(data, meta);
    }
}

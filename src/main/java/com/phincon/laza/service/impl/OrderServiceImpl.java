package com.phincon.laza.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.midtrans.httpclient.error.MidtransError;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.dto.request.CheckoutRequest;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.OrderRepository;
import com.phincon.laza.service.*;
import com.phincon.laza.utils.GenerateRandom;
import com.phincon.laza.validator.OrderValidator;
import com.xendit.exception.XenditException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private XenditService xenditService;

    @Autowired
    private MidtransService midtransService;

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
            order.setId(generateOrderId());
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(order.getCreatedAt());
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public Order requestCreateOrder(String userId, CheckoutRequest checkoutRequest) {
        try {
            User user = userService.getById(userId);

            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(checkoutRequest.getPaymentMethodId());

            if (!paymentMethod.getIsActive()) {
                throw new NotProcessException("payment method inactive");
            }

            Order order = new Order();

            order.setOrderStatus("requested");

            // Todo: implement add product order detail

            // Todo: implement add address order detail

            // Todo: implement amount from user cart
            order.setAmount(88000);

            order.setAdminFee(1000);

            // Todo: implement get shipping fee
            order.setShippingFee(9000);

            order.setUser(user);
            order.setExpiryDate(LocalDateTime.now().plusDays(1));
            Order createdOrder = createOrder(order);

            PaymentDetail paymentDetail = new PaymentDetail();

            // check if using CC
            if (checkoutRequest.getPaymentMethod().equalsIgnoreCase("credit_card")) {

            } else {


                // xendet payment gateway
                if (paymentMethod.getProvider().equalsIgnoreCase("xendit")) {
                    if (paymentMethod.getType().equalsIgnoreCase("e-wallet")) {
                        paymentDetail = xenditService.chargeEwallet(paymentMethod, order, checkoutRequest.getCallbackUrl());
                    } else if (paymentMethod.getType().equalsIgnoreCase("virtual_account")) {
                        paymentDetail = xenditService.chargeVirtualAccount(paymentMethod, order);
                    }
                } else if (paymentMethod.getProvider().equalsIgnoreCase("midtrans")) {
                    if (paymentMethod.getType().equalsIgnoreCase("e-wallet")) {
                        paymentDetail = midtransService.chargeGopay(paymentMethod, order, checkoutRequest.getCallbackUrl());
                    }
                }
            }

//            if (order.getOrderStatus().equalsIgnoreCase("requested")) {
//                throw new NotProcessException("");
//            }
            updateOrder(order.getId(), order);

            createdOrder.setPaymentDetail(paymentDetail);

            return createdOrder;
        } catch (XenditException e) {
            throw new NotProcessException(e.getMessage());
        } catch (MidtransError | JsonProcessingException e) {
            throw new RuntimeException(e);
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

    private String generateOrderId() {
        LocalDateTime date = LocalDateTime.now();
        String orderId = String.format("ORD-%02d%02d%s-", date.getDayOfMonth(), date.getMonthValue(), date.getYear());

        String result = orderId + GenerateRandom.generateRandomNumber(10);

        while (orderRepository.existsById(result)) {
            result = orderId + GenerateRandom.generateRandomNumber(10);
        }

        return result;
    }


}

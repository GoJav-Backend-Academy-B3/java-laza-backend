package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.xendit.ewallet.EwalletCallbackRequest;
import com.phincon.laza.model.dto.xendit.ewallet.XenditEwalletData;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackCreated;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackRequest;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.Transaction;
import com.phincon.laza.service.OrderService;
import com.phincon.laza.service.TransactionService;
import com.phincon.laza.service.XenditCallbackService;
import com.xendit.exception.XenditException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class XenditCallbackServiceImpl implements XenditCallbackService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void callbackEwallet(EwalletCallbackRequest ewalletCallbackRequest) throws XenditException {

        // transaction notification
        if (ewalletCallbackRequest.getEvent().equals("ewallet.capture")) {
            XenditEwalletData callbackData = ewalletCallbackRequest.getData();

            Order order = orderService.getOrderById(callbackData.getReferenceId());

            Transaction transaction = transactionService.getTransactionByReferenceId(callbackData.getId());
            transaction.setTransactionStatus(callbackData.getStatus());
            transaction.setUpdatedAt(callbackData.getUpdated());

            if (callbackData.getStatus().equals("SUCCEEDED")) {
                order.setOrderStatus("paid");
            }
            order.setPaidAt(callbackData.getUpdated());

            transactionService.updateTransaction(transaction.getId(), transaction);

            orderService.updateOrder(order.getId(), order);

            // todo: implement push notification

        } else {
            throw new XenditException("unknown callback");
        }
    }

    @Override
    public void callbackFVAPaid(FVACallbackRequest fvaCallbackRequest) {
        Order order = orderService.getOrderById(fvaCallbackRequest.getExternalId());

        Transaction transaction = transactionService.getTransactionByReferenceId(fvaCallbackRequest.getId());
        transaction.setTransactionStatus("SUCCEEDED");
        transaction.setUpdatedAt(fvaCallbackRequest.getUpdated());

        // todo: implement overpayment
        if (fvaCallbackRequest.getAmount() == order.getAmount()) {
            order.setOrderStatus("paid");
        } else if (fvaCallbackRequest.getAmount() > order.getAmount()) { // todo: implement overpayment
            order.setOrderStatus("overpayment");
        } else if (fvaCallbackRequest.getAmount() < order.getAmount()) { // todo: implement insufficient payment
            order.setOrderStatus("insufficient payment");
        }

        transactionService.updateTransaction(transaction.getId(), transaction);

        order.setPaidAt((fvaCallbackRequest.getTransactionTimestamp()));

        orderService.updateOrder(order.getId(), order);

        // todo: implement push payment notification
    }

    @Override
    public void callbackFVACreate(FVACallbackCreated fvaCallbackCreated) {
        Order order = orderService.getOrderById(fvaCallbackCreated.getExternalId());

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setReferenceId(fvaCallbackCreated.getId());
        transaction.setAmount(Math.toIntExact(fvaCallbackCreated.getExpectedAmount()));
        transaction.setProvider("xendit");
        transaction.setType("virtual_account");
        transaction.setTransactionStatus(fvaCallbackCreated.getStatus());
        transaction.setCurrency("IDR");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        order.setExpiryDate(fvaCallbackCreated.getExpirationDate());

        transactionService.createTransaction(transaction);

        orderService.updateOrder(order.getId(), order);

        // todo: implement push invoice created notification
    }
}

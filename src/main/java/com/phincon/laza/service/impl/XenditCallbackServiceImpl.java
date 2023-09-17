package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.xendit.ewallet.EwalletCallbackRequest;
import com.phincon.laza.model.dto.xendit.ewallet.XenditEwalletData;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackCreated;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackRequest;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.Transaction;
import com.phincon.laza.service.OrderService;
import com.phincon.laza.service.PushNotificationService;
import com.phincon.laza.service.TransactionService;
import com.phincon.laza.service.XenditCallbackService;
import com.xendit.exception.XenditException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.phincon.laza.utils.DateConverter.convertDateTime;

@Service
public class XenditCallbackServiceImpl implements XenditCallbackService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PushNotificationService pushNotificationService;
    @Override
    public void callbackEwallet(EwalletCallbackRequest ewalletCallbackRequest) throws XenditException {

        // transaction notification
        if (ewalletCallbackRequest.getEvent().equals("ewallet.capture")) {
            XenditEwalletData callbackData = ewalletCallbackRequest.getData();

            Order order = orderService.getOrderById(callbackData.getReferenceId());

            Transaction transaction = transactionService.getTransactionByReferenceId(callbackData.getId());
            transaction.setTransactionStatus(callbackData.getStatus());
            transaction.setUpdatedAt(convertDateTime(callbackData.getUpdated()));

            if (callbackData.getStatus().equals("SUCCEEDED")) {
                order.setOrderStatus("paid");
                order.setPaidAt(convertDateTime(callbackData.getUpdated()));

                // push notification
                String userId = order.getUser().getId();

                if (userId != null) {
                    pushNotificationService.sendPushNotification("laza_notif", userId, String.format("Order %s has been successfully paid, thank you", order.getId()));
                }
            }

            transactionService.updateTransaction(transaction.getId(), transaction);

            orderService.updateOrder(order.getId(), order);

        } else {
            throw new XenditException("unknown callback");
        }
    }

    @Override
    public void callbackFVAPaid(FVACallbackRequest fvaCallbackRequest) {
        Order order = orderService.getOrderById(fvaCallbackRequest.getExternalId());

        Transaction transaction = transactionService.getTransactionByReferenceId(fvaCallbackRequest.getId());
        transaction.setTransactionStatus("SUCCEEDED");
        transaction.setUpdatedAt(convertDateTime(fvaCallbackRequest.getUpdated()));

        if (fvaCallbackRequest.getAmount() == order.getAmount()) {
            order.setOrderStatus("paid");
        } else if (fvaCallbackRequest.getAmount() > order.getAmount()) {
            order.setOrderStatus("overpayment");
        } else if (fvaCallbackRequest.getAmount() < order.getAmount()) {
            order.setOrderStatus("insufficient payment");
        }

        transactionService.updateTransaction(transaction.getId(), transaction);

        order.setPaidAt(convertDateTime(fvaCallbackRequest.getTransactionTimestamp()));

        orderService.updateOrder(order.getId(), order);

        // push payment notification
        pushNotificationService.sendPushNotification("laza_notif", order.getUser().getId(), String.format("Order %s has been successfully paid, thank you", order.getId()));

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

        order.setExpiryDate(convertDateTime(fvaCallbackCreated.getExpirationDate()));

        transactionService.createTransaction(transaction);

        orderService.updateOrder(order.getId(), order);

        // push invoice notification
        pushNotificationService.sendPushNotification("laza_notif", order.getUser().getId(), String.format("Order %s has been successfully created", order.getId()));
    }
}

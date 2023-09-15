package com.phincon.laza.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.phincon.laza.model.dto.midtrans.gopay.GoPayTransaction;
import com.phincon.laza.model.dto.midtrans.gopay.TransactionAction;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.model.entity.Transaction;
import com.phincon.laza.service.MidtransService;
import com.phincon.laza.service.PaymentDetailService;
import com.phincon.laza.service.TransactionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.midtrans.httpclient.TransactionApi.checkTransaction;

@Service
public class MidtransServiceImpl implements MidtransService {
    @Autowired
    private MidtransCoreApi coreApi;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentDetailService paymentDetailService;

    @Override
    public PaymentDetail chargeGopay(PaymentMethod paymentMethod, Order order, String callbackUrl) throws MidtransError, JsonProcessingException {
        Map<String, Object> chargeParams = new HashMap<>();

        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", order.getId());
        transactionDetails.put("gross_amount", order.getAmount());

        Map<String, Object> callback = new HashMap<>();
        callback.put("enable_callback", true);
        callback.put("callback_url", callbackUrl);

        chargeParams.put("transaction_details", transactionDetails);
        chargeParams.put("payment_type", "gopay");
        chargeParams.put("gopay", callback);

        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject object = coreApi.chargeTransaction(chargeParams);

        // Convert JSON object to Class
        GoPayTransaction goPayTransaction = objectMapper.readValue(object.toString(), GoPayTransaction.class);

        order.setOrderStatus(goPayTransaction.getTransactionStatus());

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setReferenceId(goPayTransaction.getTransactionId());

        double amount = Double.parseDouble(goPayTransaction.getGrossAmount());
        transaction.setAmount((int) amount);
        transaction.setProvider("midtrans");
        transaction.setCurrency(goPayTransaction.getCurrency());
        transaction.setTransactionStatus(goPayTransaction.getTransactionStatus());
        transaction.setType("gopay");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        if (order.getTransaction() == null) {
            List<Transaction> transactions = new ArrayList<>();
            order.setTransaction(transactions);
        }

//        order.setTransaction(transactionService.createTransaction(transaction));
        order.getTransaction().add(transactionService.createTransaction(transaction));

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentMethod(paymentMethod.getName());
        paymentDetail.setType(paymentMethod.getType());
        paymentDetail.setCode(paymentMethod.getCode());
        paymentDetail.setProvider("midtrans");
        if (goPayTransaction.getActions() != null) {
            List<TransactionAction> actions = goPayTransaction.getActions();

            paymentDetail.setQrCode(actions.get(0).getUrl());
            paymentDetail.setDeeplink(actions.get(1).getUrl());
        }
        paymentDetail.setOrder(order);

        return paymentDetailService.createPaymentDetail(paymentDetail);

    }
}

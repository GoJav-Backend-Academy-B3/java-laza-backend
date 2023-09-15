package com.phincon.laza.service.impl;

import com.midtrans.httpclient.error.MidtransError;
import com.phincon.laza.model.dto.midtrans.gopay.GoPayCallbackRequest;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.Transaction;
import com.phincon.laza.service.MidtransCallbackService;
import com.phincon.laza.service.OrderService;
import com.phincon.laza.service.TransactionService;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.phincon.laza.utils.DateConverter.convertDateTime;

@Service
public class MidtransCallbackServiceImpl implements MidtransCallbackService {

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void callbackGopay(GoPayCallbackRequest goPayCallbackRequest) throws MidtransError, NoSuchAlgorithmException {
        String signatureKey = goPayCallbackRequest.getOrderId() +
                goPayCallbackRequest.getStatusCode() +
                goPayCallbackRequest.getGrossAmount() +
                MIDTRANS_SERVER_KEY;

        String signatureKeyHash = generateSignatureKeyHash(signatureKey);

        if (goPayCallbackRequest.getSignatureKey().equals(signatureKeyHash)) {

            Order order = orderService.getOrderById(goPayCallbackRequest.getOrderId());

            Transaction transaction = transactionService.getTransactionByReferenceId(goPayCallbackRequest.getTransactionId());

            if (goPayCallbackRequest.getTransactionStatus().equals("settlement")) {
                order.setOrderStatus("paid");
                transaction.setTransactionStatus(goPayCallbackRequest.getTransactionStatus());
                transaction.setUpdatedAt(convertDateTime(goPayCallbackRequest.getSettlementTime()));

            }

            transactionService.updateTransaction(transaction.getId(), transaction);

            order.setPaidAt(convertDateTime(goPayCallbackRequest.getSettlementTime()));
            orderService.updateOrder(order.getId(), order);

        } else {
            throw new MidtransError("unknown callback");
        }
    }

    private String generateSignatureKeyHash(String signatureKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] encodedHash = digest.digest(signatureKey.getBytes(StandardCharsets.UTF_8));

        return DatatypeConverter.printHexBinary(encodedHash).toLowerCase();
    }
}

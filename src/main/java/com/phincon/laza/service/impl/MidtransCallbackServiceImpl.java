package com.phincon.laza.service.impl;

import com.midtrans.httpclient.error.MidtransError;
import com.phincon.laza.model.dto.midtrans.gopay.GoPayCallbackRequest;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.service.MidtransCallbackService;
import com.phincon.laza.service.OrderService;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class MidtransCallbackServiceImpl implements MidtransCallbackService {

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Autowired
    private OrderService orderService;

    @Override
    public void callbackGopay(GoPayCallbackRequest goPayCallbackRequest) throws MidtransError, NoSuchAlgorithmException {
        String signatureKey = goPayCallbackRequest.getOrder_id() +
                goPayCallbackRequest.getStatus_code() +
                goPayCallbackRequest.getGross_amount() +
                MIDTRANS_SERVER_KEY;

       String signatureKeyHash = generateSignatureKey(signatureKey);


        if (goPayCallbackRequest.getSignature_key().equals(signatureKeyHash)) {

            Order order = orderService.getOrderById(goPayCallbackRequest.getTransaction_id());

            if (goPayCallbackRequest.getTransaction_status().equals("settlement")) {
                order.setOrderStatus("paid");
            }

            // TODO: Implement get by reference id
            // TODO: Implement update transaction

            order.setPaidAt(LocalDateTime.parse(goPayCallbackRequest.getSettlement_time()));
            orderService.updateOrder(order.getId(), order);

        } else {
            throw new MidtransError("unknown callback");
        }
    }

    private String generateSignatureKey(String signatureKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] encodedHash = digest.digest(signatureKey.getBytes(StandardCharsets.UTF_8));

       return DatatypeConverter.printHexBinary(encodedHash).toLowerCase();
    }
}

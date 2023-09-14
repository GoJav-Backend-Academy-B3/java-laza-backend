package com.phincon.laza.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.phincon.laza.model.dto.midtrans.CreditCardTokenData;
import com.phincon.laza.model.dto.midtrans.CreditCardTokenRequest;
import com.phincon.laza.model.dto.midtrans.CreditCardTokenResponse;
import com.phincon.laza.model.dto.midtrans.TransactionDataRequest;
import com.phincon.laza.model.dto.midtrans.TransactionDataResponse;
import com.phincon.laza.model.dto.midtrans.TransactionDetailsData;
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
    public PaymentDetail chargeGopay(PaymentMethod paymentMethod, Order order, String callbackUrl)
            throws MidtransError, JsonProcessingException {
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

        order.setOrderStatus(goPayTransaction.getTransaction_status());

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setReferenceId(goPayTransaction.getTransaction_id());

        double amount = Double.parseDouble(goPayTransaction.getGross_amount());
        transaction.setAmount((int) amount);
        transaction.setProvider("midtrans");
        transaction.setCurrency(goPayTransaction.getCurrency());
        transaction.setTransactionStatus(goPayTransaction.getTransaction_status());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        order.setTransaction(transactionService.createTransaction(transaction));

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

    @Override
    public PaymentDetail chargeCreditCard(CreditCardTokenRequest request, PaymentMethod method, Order order,
            String callbackUrl)
            throws MidtransError, JsonProcessingException, Exception {
        var mapper = new ObjectMapper();

        Map<String, String> requestToMap = mapper.convertValue(request,
                new TypeReference<Map<String, String>>() {
                });
        JSONObject jTokenResponse = coreApi.cardToken(requestToMap);

        CreditCardTokenResponse tokenResponse = mapper.convertValue(jTokenResponse.toString(),
                CreditCardTokenResponse.class);
        if (Integer.parseInt(tokenResponse.statusCode()) != 200) {
            // TODO Finer exception throw
            throw new Exception();
        }

        TransactionDataRequest transactionRequest = new TransactionDataRequest("credit_card",
                new CreditCardTokenData(tokenResponse.tokenId()),
                new TransactionDetailsData(order.getId(), order.getAmount()));
        Map<String, Object> transactionRequestToMap = mapper.convertValue(transactionRequest,
                new TypeReference<Map<String, Object>>() {
                });
        JSONObject jTransactionResponse = coreApi.chargeTransaction(transactionRequestToMap);

        TransactionDataResponse transactionResponse = mapper.convertValue(jTransactionResponse.toString(),
                TransactionDataResponse.class);

        if (!transactionResponse.transactionStatus().equalsIgnoreCase("capture")
                && !transactionResponse.fraudStatus().equalsIgnoreCase("accept")) {
            // TODO final exception throwing
            throw new Exception();
        }
        order.setOrderStatus(transactionResponse.transactionStatus());
        Transaction transaction = new Transaction(null,
                transactionResponse.transactionId(),
                Double.valueOf(transactionResponse.grossAmount()).intValue(), transactionResponse.statusMessage(),
                "credit_card",
                "midtrans",
                transactionResponse.currency(),
                transactionResponse.transactionStatus(),
                LocalDateTime.parse(transactionResponse.transactionTime()),
                LocalDateTime.parse(transactionResponse.transactionTime()), order);
        order.setTransaction(transactionService.createTransaction(transaction));

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentMethod(method.getName());
        paymentDetail.setType(method.getType());
        paymentDetail.setBank(transactionResponse.bank());
        paymentDetail.setCode(method.getCode());
        paymentDetail.setProvider(method.getProvider());
        paymentDetail.setCreditCardNumber(transactionResponse.maskedCard());
        paymentDetail.setRedirectUrl(transactionResponse.redirectUrl());

        paymentDetail.setOrder(order);

        return paymentDetailService.createPaymentDetail(paymentDetail);
    }
}

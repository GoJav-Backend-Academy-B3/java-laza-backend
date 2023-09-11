package com.phincon.laza.service.impl;

import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.model.entity.Transaction;
import com.phincon.laza.service.PaymentDetailService;
import com.phincon.laza.service.TransactionService;
import com.phincon.laza.service.XenditService;
import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.EWalletCharge;
import com.xendit.model.FixedVirtualAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class XenditServiceImpl implements XenditService {

    @Autowired
    private XenditClient xenditClient;

//    @Autowired
//    private OrderService orderService;

    @Autowired
    private PaymentDetailService paymentDetailService;

    @Autowired
    private TransactionService transactionService;

    public PaymentDetail chargeEwallet(PaymentMethod paymentMethod, Order order, String callbackUrl) throws XenditException {
        Map<String, String> channelProperties = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("reference_id", order.getId());
        params.put("currency", "IDR");
        params.put("amount", order.getAmount());
        params.put("checkout_method", "ONE_TIME_PAYMENT");
        params.put("channel_code", paymentMethod.getCode());
        params.put("channel_properties", channelProperties);
        
        if (paymentMethod.getCode().equalsIgnoreCase("ID_OVO")) {
            // Todo: implement get phone number
            channelProperties.put("mobile_number", "+628123123123");
        } else if (paymentMethod.getCode().equalsIgnoreCase("ID_SHOPEEPAY")) {
            channelProperties.put("success_return_url", callbackUrl);
        }

        EWalletCharge eWalletCharge = xenditClient.eWallet.createEWalletCharge(params);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + eWalletCharge);

        order.setOrderStatus(eWalletCharge.getStatus().toLowerCase());

        // add transaction to database
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setReferenceId(eWalletCharge.getId());
        transaction.setAmount(Integer.valueOf(eWalletCharge.getChargeAmount()));
        transaction.setProvider("xendit");
        transaction.setCurrency(eWalletCharge.getCurrency());
        transaction.setTransactionStatus(eWalletCharge.getStatus());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionService.createTransaction(transaction);

        // add payment detail to database
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPaymentMethod(paymentMethod.getName());
        paymentDetail.setType(paymentMethod.getType());
        paymentDetail.setCode(paymentMethod.getCode());
        paymentDetail.setProvider(paymentMethod.getProvider());
        paymentDetail.setDeeplink(eWalletCharge.getCallbackUrl());
        if (eWalletCharge.getActions() != null) {
            paymentDetail.setQrCode(eWalletCharge.getActions().get("qr_checkout_string"));
        }
        paymentDetail.setOrder(order);

        return paymentDetailService.createPaymentDetail(paymentDetail);
    }

    public PaymentDetail chargeVirtualAccount(PaymentMethod paymentMethod, Order order) throws XenditException {

        Map<String, Object> params = new HashMap<>();
        params.put("external_id", order.getId());
        params.put("bank_code", paymentMethod.getCode());
        params.put("name", order.getUser().getFullName());
        params.put("expected_amount", order.getAmount());

        FixedVirtualAccount closedVirtualAccount = xenditClient.fixedVirtualAccount.createClosed(params);

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setType(paymentMethod.getType());
        paymentDetail.setCode(paymentMethod.getCode());
        paymentDetail.setBank(closedVirtualAccount.getBankCode());
        paymentDetail.setVaNumber(closedVirtualAccount.getAccountNumber());
        paymentDetail.setOrder(order);

        return paymentDetailService.createPaymentDetail(paymentDetail);
    }
}

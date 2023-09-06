package com.phincon.laza.service.impl;

import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.service.PaymentDetailService;
import com.phincon.laza.service.XenditService;
import com.xendit.XenditClient;
import com.xendit.exception.XenditException;
import com.xendit.model.EWalletCharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class XenditServiceImpl implements XenditService {

    @Autowired
    private XenditClient xenditClient;

    @Autowired
    private PaymentDetailService paymentDetailService;

    public PaymentDetail chargeEwallet(PaymentMethod paymentMethod, Order order) throws XenditException {
        Map<String, String> channelProperties = new HashMap<>();
        channelProperties.put("success_return_url", "https://redirect.me/goodstuff");

        Map<String, Object> params = new HashMap<>();
        params.put("reference_id", order.getId());
        params.put("currency", "IDR");
        params.put("amount", order.getAmount());
        params.put("checkout_method", "ONE_TIME_PAYMENT");
        params.put("channel_code", paymentMethod.getCode());
        params.put("channel_properties", channelProperties);

        EWalletCharge eWalletCharge = xenditClient.eWallet.createEWalletCharge(params);

        System.out.println(eWalletCharge);
        System.out.println(eWalletCharge.getChannelProperties());
        System.out.println(eWalletCharge.getActions());

        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setType(order.getPaymentMethod().getType());
        paymentDetail.setCode(order.getPaymentMethod().getCode());
        paymentDetail.setDeeplink(eWalletCharge.getActions().get("mobile_deeplink_checkout_url"));
        paymentDetail.setQrCode(eWalletCharge.getActions().get("qr_checkout_string"));
        paymentDetail.setOrder(order);

        return paymentDetailService.createPaymentDetail(paymentDetail);
    }
}

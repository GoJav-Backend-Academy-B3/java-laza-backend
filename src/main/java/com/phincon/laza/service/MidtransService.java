package com.phincon.laza.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.midtrans.httpclient.error.MidtransError;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.model.entity.PaymentMethod;

public interface MidtransService {

    PaymentDetail chargeGopay(PaymentMethod paymentMethod, Order order, String callbackUrl) throws MidtransError, JsonProcessingException;

}

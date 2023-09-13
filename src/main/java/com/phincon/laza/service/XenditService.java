package com.phincon.laza.service;

import com.phincon.laza.model.entity.Order;
import com.phincon.laza.model.entity.PaymentDetail;
import com.phincon.laza.model.entity.PaymentMethod;
import com.xendit.exception.XenditException;

public interface XenditService {
    public PaymentDetail chargeEwallet(PaymentMethod paymentMethod, Order order, String callbackUrl) throws XenditException;

    public PaymentDetail chargeVirtualAccount(PaymentMethod paymentMethod, Order order) throws XenditException;

}

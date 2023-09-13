package com.phincon.laza.service.impl;

import com.phincon.laza.model.dto.xendit.ewallet.EwalletCallbackRequest;
import com.phincon.laza.model.dto.xendit.ewallet.XenditEwalletData;
import com.phincon.laza.model.entity.Order;
import com.phincon.laza.service.OrderService;
import com.phincon.laza.service.XenditCallbackService;
import com.xendit.exception.XenditException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.phincon.laza.utils.DateConverter.convertDateTime;

@Service
public class XenditCallbackServiceImpl implements XenditCallbackService {

    @Autowired
    private OrderService orderService;

    @Override
    public void callbackEwallet(EwalletCallbackRequest ewalletCallbackRequest) throws XenditException {

        // transaction notification
        if (ewalletCallbackRequest.getEvent().equals("ewallet.capture")) {
            XenditEwalletData callbackData = ewalletCallbackRequest.getData();

            Order order = orderService.getOrderById(callbackData.getReferenceId());

            if (callbackData.getStatus().equals("SUCCEEDED")) {
                order.setOrderStatus("paid");
            }
            order.setPaidAt(convertDateTime(callbackData.getUpdated()));

            orderService.updateOrder(order.getId(), order);

            // todo: implement push notification

        } else {
            throw new XenditException("unknown callback");
        }
    }
}

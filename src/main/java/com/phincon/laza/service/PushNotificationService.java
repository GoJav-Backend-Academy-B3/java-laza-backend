package com.phincon.laza.service;

import com.phincon.laza.model.entity.Order;

public interface PushNotificationService {
//    void sendPushNotification(String exchange, String routingKey, Object message);
    void sendPushNotification(String message, String userId);

//    void sendPushPaidOrderNotification(String exchange, String routingKey, Order order);
}

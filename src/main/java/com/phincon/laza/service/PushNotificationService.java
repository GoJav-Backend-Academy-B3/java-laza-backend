package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.CartRequest;
import com.phincon.laza.model.entity.Cart;

public interface PushNotificationService {
    void sendPushNotification(String userId, String message);

}

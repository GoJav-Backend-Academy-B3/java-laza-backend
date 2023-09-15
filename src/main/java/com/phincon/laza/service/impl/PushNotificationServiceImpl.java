package com.phincon.laza.service.impl;


import com.phincon.laza.config.RabbitMqConfig;
import com.phincon.laza.service.PushNotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PushNotificationServiceImpl implements PushNotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PushNotificationServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendPushNotification(String userId, String message) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE,
                RabbitMqConfig.ROUTING_KEY,
                message
        );
    }
}

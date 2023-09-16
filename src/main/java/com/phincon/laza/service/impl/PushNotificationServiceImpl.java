package com.phincon.laza.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.entity.Order;
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
    public void sendPushNotification(String exchange, String routingKey, Object data) {

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                data
        );
    }

    @Override
    public void sendPushPaidOrderNotification(String exchange, String routingKey, Order data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(data);
            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    jsonMessage.getBytes()
            );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

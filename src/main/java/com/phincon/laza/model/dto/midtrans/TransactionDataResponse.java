package com.phincon.laza.model.dto.midtrans;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDataResponse(
        String statusCode,
        String statusMessage,
        String transactionId,
        String orderId,
        String redirectUrl,
        String grossAmount,
        String currency,
        String paymentType,
        String transactionTime,
        String transactionStatus,
        String fraudStatus,
        String maskedCard,
        String bank,
        String cardType) {
}

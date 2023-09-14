package com.phincon.laza.model.dto.midtrans;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreditCardTokenRequest(
        String cardNumber,
        String cardExpMonth,
        String cardExpYear,
        String cardCvv) {
}

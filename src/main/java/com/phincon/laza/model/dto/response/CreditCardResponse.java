package com.phincon.laza.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phincon.laza.model.entity.CreditCard;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreditCardResponse(
        String id,
        int expiryMonth,
        int expiryYear,
        String cardNumber) {
    public static CreditCardResponse fromEntity(CreditCard e) {
        return new CreditCardResponse(e.getId(), e.getExpiryMonth(), e.getExpiryYear(), e.getCardNumber());
    }
}

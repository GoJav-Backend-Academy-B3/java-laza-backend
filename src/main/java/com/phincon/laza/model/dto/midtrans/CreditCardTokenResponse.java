package com.phincon.laza.model.dto.midtrans;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreditCardTokenResponse(
        String statusCode,
        String statusMessage,
        String tokenId,
        String hash) {
}

package com.phincon.laza.model.dto.midtrans;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDataRequest(
    String paymentType,
    CreditCardTokenData creditCard,
    TransactionDetailsData transactionDetails
) {
}

package com.phincon.laza.model.dto.xendit.fva;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FVACallbackRequest {
    private String id;
    private int amount;
    private String country;
    private String created;
    private String updated;
    private String currency;
    private String ownerId;
    private String bankCode;
    private String paymentId;
    private String externalId;
    private String merchantCode;
    private String accountNumber;
    private String transactionTimestamp;
    private String callbackVirtualAccountId;
}

package com.phincon.laza.model.dto.xendit.fva;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FVACallbackRequest {
    private String id;
    private int amount;
    private String country;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String currency;
    private String ownerId;
    private String bankCode;
    private String paymentId;
    private String externalId;
    private String merchantCode;
    private String accountNumber;
    private LocalDateTime transactionTimestamp;
    private String callbackVirtualAccountId;
}

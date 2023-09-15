package com.phincon.laza.model.dto.xendit.fva;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FVACallbackCreated {
    private String id;
    private String name;
    private String status;
    private String country;
    private String created;
    private String updated;
    private String currency;
    private String ownerId;
    private String bankCode;
    private boolean isClosed;
    private String externalId;
    private boolean isSingleUse;
    private String merchantCode;
    private String accountNumber;
    private int expectedAmount;
    private String expirationDate;
}

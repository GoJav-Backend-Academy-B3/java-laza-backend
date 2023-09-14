package com.phincon.laza.model.dto.xendit.fva;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FVACallbackCreated {
    private String id;
    private String name;
    private String status;
    private String country;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private LocalDateTime updated;

    private String currency;
    @JsonProperty("owner_id")
    private String ownerId;

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("is_closed")
    private boolean isClosed;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("is_single_use")
    private boolean isSingleUse;

    @JsonProperty("merchant_code")
    private String merchantCode;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("expected_amount")
    private int expectedAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private LocalDateTime expirationDate;
}

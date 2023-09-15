package com.phincon.laza.model.dto.midtrans.gopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoPayCallbackRequest {
    private String transactionTime;
    private String transactionStatus;
    private String transactionId;
    private String statusMessage;
    private String statusCode;
    private String signatureKey;
    private String settlementTime;
    private String paymentType;
    private String orderId;
    private String merchantId;
    private String grossAmount;
    private String fraudStatus;
    private String currency;
}

package com.phincon.laza.model.dto.midtrans.gopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoPayTransaction {
    private String statusCode;
    private String statusMessage;
    private String transactionId;
    private String orderId;
    private String merchantId;
    private String grossAmount;
    private String currency;
    private String paymentType;
    private String transactionTime;
    private String transactionStatus;
    private String fraudStatus;
    private List<TransactionAction> actions;
    private String expiryTime;
}

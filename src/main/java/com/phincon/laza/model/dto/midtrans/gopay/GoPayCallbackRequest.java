package com.phincon.laza.model.dto.midtrans.gopay;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoPayCallbackRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionTime;
    private String transactionStatus;
    private String transactionId;
    private String statusMessage;
    private String statusCode;
    private String signatureKey;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementTime;
    private String paymentType;
    private String orderId;
    private String merchantId;
    private String grossAmount;
    private String fraudStatus;
    private String currency;
}

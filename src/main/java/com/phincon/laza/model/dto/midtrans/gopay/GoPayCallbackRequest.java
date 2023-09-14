package com.phincon.laza.model.dto.midtrans.gopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoPayCallbackRequest {
    private String transaction_time;
    private String transaction_status;
    private String transaction_id;
    private String status_message;
    private String status_code;
    private String signature_key;
    private String settlement_time;
    private String payment_type;
    private String order_id;
    private String merchant_id;
    private String gross_amount;
    private String fraud_status;
    private String currency;
}

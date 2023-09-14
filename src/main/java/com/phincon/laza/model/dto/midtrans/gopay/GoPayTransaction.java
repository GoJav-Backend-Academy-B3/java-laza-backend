package com.phincon.laza.model.dto.midtrans.gopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoPayTransaction {
    private String status_code;
    private String status_message;
    private String transaction_id;
    private String order_id;
    private String merchant_id;
    private String gross_amount;
    private String currency;
    private String payment_type;
    private String transaction_time;
    private String transaction_status;
    private String fraud_status;
    private List<TransactionAction> actions;
    private String expiry_time;
}

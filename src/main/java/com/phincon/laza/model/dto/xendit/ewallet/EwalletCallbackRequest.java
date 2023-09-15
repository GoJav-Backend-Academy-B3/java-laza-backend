package com.phincon.laza.model.dto.xendit.ewallet;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EwalletCallbackRequest {
    private XenditEwalletData data;
    private String event;
    private String created;
    private String businessId;
}
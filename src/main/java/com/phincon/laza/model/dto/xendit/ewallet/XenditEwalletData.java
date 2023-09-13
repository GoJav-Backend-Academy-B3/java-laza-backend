package com.phincon.laza.model.dto.xendit.ewallet;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class XenditEwalletData {
    private String id;
    private Object basket;
    private String status;
    private Actions actions;
    private String created;
    private String updated;
    private String currency;
    private Map<String, String> metadata;
    private Object voidedAt;
    private boolean captureNow;
    private Object customerId;
    private String callbackUrl;
    private String channelCode;
    private Object failureCode;
    private String referenceId;
    private int chargeAmount;
    private int captureAmount;
    private String checkoutMethod;
    private Object paymentMethodId;
    private ChannelProperties channelProperties;
    private boolean isRedirectRequired;
}

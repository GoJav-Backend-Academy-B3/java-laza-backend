package com.phincon.laza.model.dto.xendit.ewallet;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class ChannelProperties {
    private String successRedirectUrl;
}


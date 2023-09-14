
package com.phincon.laza.model.dto.midtrans;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreditCardTokenData(
    String tokenId
) {
    @JsonGetter
    public boolean authentication() {
        return false;
    }
}

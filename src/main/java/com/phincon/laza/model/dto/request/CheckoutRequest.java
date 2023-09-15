package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckoutRequest {
    @NotNull
    private String paymentMethod;

    @NotNull
    private Long paymentMethodId;

    @NotNull
    private Long addressId;

    private String callbackUrl;

    @NotNull
    private String courier;

    private String creditCardId;
    private String creditCardCVV;
}

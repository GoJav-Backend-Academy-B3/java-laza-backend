package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateUpdateCreditCardRequest(
        @Valid @NotBlank @Digits(integer = 16, fraction = 0) String cardNumber,
        @Valid @NotNull @Min(1) @Max(12) int expiryMonth,
        @Valid @NotNull int expiryYear) {
}

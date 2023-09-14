package com.phincon.laza.model.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ROCostRequest {
    @NotBlank(message = "origin is required")
    private String origin;

    @NotBlank(message = "destination is required")
    private String destination;

    @Min(value = 1, message = "The minimum weight is 1")
    @Max(value = 30000,message = "The maximum weight is 30000")
    @NotNull(message = "weight is required")
    private Integer weight;

    @NotBlank(message = "courier is required")
    private String courier;
}

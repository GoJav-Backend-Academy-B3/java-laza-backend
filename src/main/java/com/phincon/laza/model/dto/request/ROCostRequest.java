package com.phincon.laza.model.dto.request;


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
    @NotNull(message = "weight is required")
    private Integer weight;
    @NotBlank(message = "courier is required")
    private String courier;
}

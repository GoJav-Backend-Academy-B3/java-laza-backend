package com.phincon.laza.model.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    private String userId;

    @NotNull(message = "product id is required")
    private Long productId;

    @NotNull(message = "sizeId id is required")
    private Long sizeId;
}

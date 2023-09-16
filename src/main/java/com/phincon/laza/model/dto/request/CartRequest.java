package com.phincon.laza.model.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {

    @NotNull(message = "product_id id is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "size_id id is required")
    @JsonProperty("size_id")
    private Long sizeId;

    @NotNull(message = "quantity is required")
    @JsonProperty("quantity")
    private Integer quantity;
}

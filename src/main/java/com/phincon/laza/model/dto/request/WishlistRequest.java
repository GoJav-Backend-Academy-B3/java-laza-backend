package com.phincon.laza.model.dto.request;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistRequest {
    @NotNull(message = "product_id is required")
    @JsonProperty("product_id")
    private Long productId;
}

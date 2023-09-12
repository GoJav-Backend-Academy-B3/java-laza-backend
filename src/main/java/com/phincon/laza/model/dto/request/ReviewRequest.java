package com.phincon.laza.model.dto.request;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    @NotNull(message = "product id is required")
    private Long productId;

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "rating is required")
    @DecimalMax(value = "5.0", message = "Rating should not exceed 5")
    private float rating;

}

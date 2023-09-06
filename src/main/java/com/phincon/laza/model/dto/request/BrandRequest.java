package com.phincon.laza.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "logo must be required")
    private String logoUrl;
}

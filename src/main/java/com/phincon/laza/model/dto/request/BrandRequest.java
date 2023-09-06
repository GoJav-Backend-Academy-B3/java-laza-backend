package com.phincon.laza.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class BrandRequest {
    @NotBlank(message = "name is required")
    private String name;

    private MultipartFile logoUrl;
}

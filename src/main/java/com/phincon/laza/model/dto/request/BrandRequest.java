package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class BrandRequest {
    @NotBlank(message = "name is required")
    private String name;

    @JsonProperty("logo_url")
    private MultipartFile logoUrl;
}

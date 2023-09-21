package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CloudinaryUploadImageResponseVerifyRequest {
    @NotBlank
    String responsePublicId;
    @NotBlank
    String responseSecureUrl;
    @NotNull
    Long responseVersion;
    @NotBlank
    String responseSignature;
}

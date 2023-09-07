package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChangePasswordRequest {
    @Size(min = 8, message = "password must be minimum 8 characters")
    @NotBlank(message = "old password is required")
    private String oldPassword;

    @Size(min = 8, message = "password must be minimum 8 characters")
    @NotBlank(message = "new password is required")
    private String newPassword;

    @Size(min = 8, message = "password must be minimum 8 characters")
    @NotBlank(message = "confirm password is required")
    private String confirmPassword;
}

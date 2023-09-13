package com.phincon.laza.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeRequest {
    @Email(message = "input must be an email format")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "code is required")
    @Size(max = 4, message = "code must be minimum 4 characters")
    private String code;
}

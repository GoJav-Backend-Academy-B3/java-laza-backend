package com.phincon.laza.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryRequest {
    @Email(message = "input must be an email format")
    @NotBlank(message = "email is required")
    private String email;
}

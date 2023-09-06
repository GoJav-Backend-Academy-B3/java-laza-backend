package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {
    @NotBlank(message = "fullName is required")
    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "fullName must be alpha")
    private String fullName;

    @NotBlank(message = "username is required")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "username must be alphanumeric and not whitespace")
    private String username;

    @Email(message = "input must be an email format")
    @NotBlank(message = "email is required")
    private String email;

    @Size(min = 8, message = "password must be minimum 8 characters")
    @NotBlank(message = "password is required")
    private String password;
}

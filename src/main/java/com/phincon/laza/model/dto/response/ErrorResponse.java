package com.phincon.laza.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer statusCode;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object subError;
}

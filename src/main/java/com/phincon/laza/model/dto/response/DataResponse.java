package com.phincon.laza.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataResponse<T> {
    private Integer statusCode;
    private String message;
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object metadata;

    public static <T> ResponseEntity<DataResponse<T>> ok(@Nullable T data) {
        DataResponse<T> body = new DataResponse<>();
        body.data = data;
        body.statusCode = HttpStatus.OK.value();
        body.message = HttpStatus.OK.name();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public static <T> ResponseEntity<DataResponse<T>> created(@Nullable T data) {
        DataResponse<T> body = new DataResponse<>();
        body.data = data;
        body.statusCode = HttpStatus.CREATED.value();
        body.message = HttpStatus.CREATED.name();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static <T> ResponseEntity<DataResponse<T>> accepted(@Nullable T data) {
        DataResponse<T> body = new DataResponse<>();
        body.data = data;
        body.statusCode = HttpStatus.ACCEPTED.value();
        body.message = HttpStatus.ACCEPTED.name();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);
    }
}

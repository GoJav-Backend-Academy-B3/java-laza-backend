package com.phincon.laza.model.dto.request;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateUpdateProductRequest {
    @NotBlank
    String name;

    @NotBlank
    String description;
    
    @NotNull
    @Min(1)
    Integer price;
    
    @NotNull
    List<Long> sizeIds;
    
    @NotNull
    Long categoryId;
    
    @NotNull
    Long brandId;
}

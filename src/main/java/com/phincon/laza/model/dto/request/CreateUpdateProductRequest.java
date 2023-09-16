package com.phincon.laza.model.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUpdateProductRequest(
        @NotBlank String name,
        @NotBlank String description,
        @Min(1) Integer price,
        @NotNull MultipartFile imageFile,
        @NotNull List<Long> sizeIds,
        @NotNull Long categoryId,
        @NotNull Long brandId) {
}

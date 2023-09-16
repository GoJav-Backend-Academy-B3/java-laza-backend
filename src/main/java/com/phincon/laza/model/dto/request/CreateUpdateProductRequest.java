package com.phincon.laza.model.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phincon.laza.validator.FileContentType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUpdateProductRequest(
        @NotBlank String name,
        @NotBlank String description,
        @Min(1) Integer price,
        @NotNull @FileContentType(contentType = {
                "image/png", "image/jpeg", "image/webp" }) MultipartFile imageFile,
        @NotNull List<Long> sizeIds,
        @NotNull Long categoryId,
        @NotNull Long brandId){
}

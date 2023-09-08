package com.phincon.laza.model.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUpdateProductRequest(
        String name,
        String description,
        Integer price,
        MultipartFile file,
        @JsonProperty("size_ids") List<Long> sizeIds,
        @JsonProperty("category_id") Long categoryId,
        @JsonProperty("brand_id") Long brandId) {
}

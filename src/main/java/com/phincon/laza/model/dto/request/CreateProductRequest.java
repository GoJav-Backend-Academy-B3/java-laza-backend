package com.phincon.laza.model.dto.request;

public record CreateProductRequest(
        String name,
        String description,
        Integer price,
        Iterable<Long> sizes,
        Long category) {
}

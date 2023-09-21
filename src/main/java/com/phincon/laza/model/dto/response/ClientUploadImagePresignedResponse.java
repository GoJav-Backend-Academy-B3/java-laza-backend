package com.phincon.laza.model.dto.response;

public record ClientUploadImagePresignedResponse(
        String publicId,
        String signature,
        String apiKey,
        long timestamp) {
}

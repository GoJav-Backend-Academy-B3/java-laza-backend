package com.phincon.laza.model.dto.cloudinary;

import java.util.Map;

public record CloudinaryUploadResult(
        String publicId,
        int width,
        int height,
        String format,
        int bytes,
        String secureUrl) {
    public static CloudinaryUploadResult fromMap(Map map) {
        return new CloudinaryUploadResult(
                (String) map.get("public_id"),
                (int) map.get("width"),
                (int) map.get("height"),
                (String) map.get("format"),
                (int) map.get("bytes"),
                (String) map.get("secure_url"));
    }

    public static CloudinaryUploadResult empty() {
        return new CloudinaryUploadResult(null, 0, 0, null, 0, null);
    }
}

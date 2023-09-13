package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class FileValidator {
    private static final long TWO_MB_IN_BYTES = 2 * 1024 * 1024;
    private static final String[] IMAGE_MIME_TYPE = new String[]{
            "image/jpg",
            "image/jpeg",
            "image/png",
            "image/webp",
    };
    public void validateMultipartFile(MultipartFile file) {
        if (Arrays.stream(IMAGE_MIME_TYPE).noneMatch(image -> image.equalsIgnoreCase(file.getContentType()))) {
            throw new BadRequestException("file format is not allowed. Please upload a JPEG, JPG, PNG or WEBP image");
        } else if (file.getSize() > TWO_MB_IN_BYTES) {
            throw new BadRequestException("file size to large, Please upload image max 2MB");
        }
    }
}

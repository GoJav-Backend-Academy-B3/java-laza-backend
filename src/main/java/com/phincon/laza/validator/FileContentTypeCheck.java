package com.phincon.laza.validator;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileContentTypeCheck implements ConstraintValidator<FileContentType, MultipartFile> {

    private String[] contentType;

    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.contentType = constraintAnnotation.contentType();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) return true;
        String fileContentType = value.getContentType();

        return Arrays.asList(contentType).contains(fileContentType);
    }

}

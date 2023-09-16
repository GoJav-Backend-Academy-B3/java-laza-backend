package com.phincon.laza.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT })
@Documented
@Constraint(validatedBy = { FileContentTypeCheck.class })
public @interface FileContentType {
    String message() default "Invalid Content-Type";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
    String[] contentType() default {};
}

package org.burningokr.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64ImageValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64Image {
    int maxSizeMB = 2;
    String errorMessage = "Image encoded as base64 string is too large. Maximal size allowed is " + maxSizeMB + " MB";

    String message() default errorMessage;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

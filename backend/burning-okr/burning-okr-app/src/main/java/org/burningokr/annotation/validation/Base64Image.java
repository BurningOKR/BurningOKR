package org.burningokr.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64ImageValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64Image {
    float maxSizeMB() default 2;

    boolean nullable() default true;

    String message() default "Image encoded as base64 string is too large or null. Maximum size allowed is {maxSizeMB} MB."; // Use the attribute in the message
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

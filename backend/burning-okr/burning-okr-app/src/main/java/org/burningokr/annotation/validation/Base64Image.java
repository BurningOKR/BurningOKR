package org.burningokr.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64ImageValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64Image {
    String message() default "Base64 String to large";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

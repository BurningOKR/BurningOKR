package org.burningokr.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Base64ImageValidator implements
        ConstraintValidator<Base64Image, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ((value.length() * (3F/4)) - 2) < 2000000;
    }

}

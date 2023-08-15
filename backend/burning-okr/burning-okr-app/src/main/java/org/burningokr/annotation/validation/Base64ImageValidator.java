package org.burningokr.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class Base64ImageValidator implements
        ConstraintValidator<Base64Image, String> {

    private final Pattern base64FormatPattern
      = Pattern.compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}={2})$");


  @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.isValidBase64String(value) && this.encodedImgHasValidSize(value);
    }

    private boolean isValidBase64String(String base64String) {
        return this.base64FormatPattern.matcher(base64String).matches();
    }

    private boolean encodedImgHasValidSize(String base64String) {
        float approxImgSizeInByte = this.approximateImageSizeUpperBoundInByte(base64String);
        return approxImgSizeInByte < Base64Image.maxSizeMB * 1000000;
    }

    private float approximateImageSizeUpperBoundInByte(String base64String) {
        return ((base64String.length() * (3F/4)) - 2);
    }

}

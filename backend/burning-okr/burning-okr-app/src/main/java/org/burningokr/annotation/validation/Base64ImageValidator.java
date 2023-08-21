package org.burningokr.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;
import java.util.regex.Pattern;

public class Base64ImageValidator implements
    ConstraintValidator<Base64Image, String> {

  public boolean nullable;
  public float maxSizeB;
  private final Pattern base64FormatPattern
      = Pattern.compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}={2})$");

  @Override
  public void initialize(Base64Image constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.nullable = constraintAnnotation.nullable();
    this.maxSizeB = constraintAnnotation.maxSizeMB() * 1000000;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (this.nullable && value == null)
      return true;
    else if (!this.nullable && value == null)
      return false;
    else {
      return this.isValidBase64String(value) && this.encodedImgHasValidSize(value);
    }
  }

  public boolean isValidBase64String(String base64String) {
    return this.base64FormatPattern.matcher(base64String).matches();
  }

  public boolean encodedImgHasValidSize(String base64String) {
    float approxImgSizeInByte = this.approximateImageSize(base64String);
    boolean approximateIsReasonable = approxImgSizeInByte <= 1.2 * this.maxSizeB;
    if (approximateIsReasonable) {
      return getImageSizeInByte(base64String) <= this.maxSizeB;
    } else {
      return false;
    }
  }

  public int getImageSizeInByte(String base64String) {
    return Base64.getDecoder().decode(base64String).length;
  }

  public float approximateImageSize(String base64String) {
    return ((base64String.length() * (3F / 4)) - 2);
  }

}

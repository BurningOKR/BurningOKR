package org.burningokr.annotation.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class Base64ImageValidatorTest {

  Base64ImageValidator validator;
  @BeforeEach
  void setUp() {
    validator = spy(new Base64ImageValidator());
  }

  @Test
  void isValid_isNullableTrueShouldReturnTrueForNullInput() {
    //assemble
    validator.nullable = true;

    //act
    boolean result = this.validator.isValid(null, null);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void isValid_isNullableFalseShouldReturnFalseForNullInput() {
    //assemble
    validator.nullable = false;

    //act
    boolean result = this.validator.isValid(null, null);

    //assert
    Assertions.assertFalse(result);
  }

  @Test
  void isValid_shouldReturnFalseForInvalidBase64ImageString() {
    //assemble
    validator.nullable = false;

    //act
    boolean result = this.validator.isValid(null, null);

    //assert
    Assertions.assertFalse(result);
  }

  @Test
  void isValid_shouldReturnFalseForInvalidBase64ImageStringAndTooLargeImageSize() {
    //assemble
    validator.nullable = false;
    doReturn(false).when(this.validator).isValidBase64String(any());
    doReturn(false).when(this.validator).encodedImgHasValidSize(any());

    //act
    boolean result = this.validator.isValid("dummy", null);

    //assert
    Assertions.assertFalse(result);
  }

  @Test
  void isValid_shouldReturnFalseForInvalidBase64ImageStringAndValidLargeImageSize() {
    //assemble
    validator.nullable = false;
    doReturn(false).when(this.validator).isValidBase64String(any());
    doReturn(true).when(this.validator).encodedImgHasValidSize(any());

    //act
    boolean result = this.validator.isValid("dummy", null);

    //assert
    Assertions.assertFalse(result);
  }

  @Test
  void isValid_shouldReturnFalseForValidBase64ImageStringButTooLargeImageSize() {
    //assemble
    validator.nullable = false;
    doReturn(true).when(this.validator).isValidBase64String(any());
    doReturn(false).when(this.validator).encodedImgHasValidSize(any());

    //act
    boolean result = this.validator.isValid("dummy", null);

    //assert
    Assertions.assertFalse(result);
  }

  @Test
  void isValid_shouldReturnTrueForValidBase64ImageStringAndValidImageSize() {
    //assemble
    validator.nullable = false;
    doReturn(true).when(this.validator).isValidBase64String(any());
    doReturn(true).when(this.validator).encodedImgHasValidSize(any());

    //act
    boolean result = this.validator.isValid("dummy", null);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void isValidBase64String_shouldReturnTrue() {
    //assemble

    //act

    //assert

  }

  @Test
  void isValidBase64String_shouldReturnFalse() {
    //assemble

    //act

    //assert

  }
}

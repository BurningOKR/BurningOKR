package org.burningokr.annotation.validation;

import org.burningokr.utils.Base64Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 public class Base64ImageValidatorTest {

  Base64ImageValidator validator;

  @BeforeEach
  void setUp() {
    validator = spy(new Base64ImageValidator());
  }

  @Test
  void initialize() {
    //assemble
    Base64Image constraintAnnotationMock = mock(Base64Image.class);
    doReturn(true).when(constraintAnnotationMock).nullable();
    doReturn(1f).when(constraintAnnotationMock).maxSizeMB();

    //act
    this.validator.initialize(constraintAnnotationMock);

    //assert
    Assertions.assertTrue(this.validator.nullable);
    Assertions.assertEquals(1024*1024, this.validator.maxSizeB);
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
    String input = Base64Strings.WHITE_IMAGE_1X1_PIXEL;
    //act
    boolean result = this.validator.isValidBase64String(input);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void isValidBase64String_shouldReturnFalse() {
    //assemble
    String input = "banane";

    //act
    boolean result = this.validator.isValidBase64String(input);

    //assert
    Assertions.assertFalse(result);
  }

  @Test
  void encodedImageHasValidSize_shouldReturnTrueOnValidApproximateSizeAndValidActualSize(){
    //assemble
    String input = "base64dummy";
    this.validator.maxSizeB = 100;
    //mock class method calls
    float barelyReasonableSize = 100 * 1.19f;
    doReturn(barelyReasonableSize).when(this.validator).approximateImageSize(input);
    int validActualSize = (int) this.validator.maxSizeB;
    doReturn(validActualSize).when(this.validator).getImageSizeInByte(input);

    //act
    boolean result = this.validator.encodedImgHasValidSize(input);

    //assert
    Assertions.assertTrue(result);
    verify(this.validator).getImageSizeInByte(input);
  }

  @Test
  void encodedImageHasValidSize_shouldReturnFalseOnValidApproximateSizeButInvalidActualSize(){
    //assemble
    String input = "base64dummy";
    this.validator.maxSizeB = 100;
    //mock class method calls
    float barelyReasonableSize = 100 * 1.19f;
    doReturn(barelyReasonableSize).when(this.validator).approximateImageSize(input);
    int invalidActualSize = (int) (this.validator.maxSizeB + 1);
    doReturn(invalidActualSize).when(this.validator).getImageSizeInByte(input);

    //act
    boolean result = this.validator.encodedImgHasValidSize(input);

    //assert
    Assertions.assertFalse(result);
    verify(this.validator).getImageSizeInByte(input);
  }

  @Test
  void encodedImageHasValidSize_shouldReturnFalseOnInvalidApproximateSize(){
    //assemble
    String input = "base64dummy";
    this.validator.maxSizeB = 100;
    //mock class method calls
    float barelyUnreasonableSize = 100 * 1.21f;
    doReturn(barelyUnreasonableSize).when(this.validator).approximateImageSize(input);

    //act
    boolean result = this.validator.encodedImgHasValidSize(input);

    //assert
    Assertions.assertFalse(result);
    verify(this.validator, never()).getImageSizeInByte(input);
  }

  @Test
  void getImageSizeInByte_shouldReturn2624Bytes() {
    //assemble
    String input = Base64Strings.IMAGE_67_BYTE;
    int expected = 67;

    //act
    int actual = this.validator.getImageSizeInByte(input);

    //assert
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void approximateImageSize_shouldReturn1() {
    //assemble
    String inputWithLength4 = "word";
    float expected = 1;

    //act
    float actual = this.validator.approximateImageSize(inputWithLength4);

    //assert
    Assertions.assertEquals(expected, actual);

  }


}

package org.burningokr.service.exceptions;

public class MissingAnnotationException extends RuntimeException {
  public MissingAnnotationException(String error) {
    super(error);
  }
}

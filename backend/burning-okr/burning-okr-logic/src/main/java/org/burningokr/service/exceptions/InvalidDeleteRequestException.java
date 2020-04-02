package org.burningokr.service.exceptions;

public class InvalidDeleteRequestException extends RuntimeException {
  public InvalidDeleteRequestException(String error) {
    super(error);
  }
}

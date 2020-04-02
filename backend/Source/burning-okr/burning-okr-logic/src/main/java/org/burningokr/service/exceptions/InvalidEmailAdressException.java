package org.burningokr.service.exceptions;

public class InvalidEmailAdressException extends RuntimeException {
  public InvalidEmailAdressException(String errorMessage) {
    super(errorMessage);
  }
}

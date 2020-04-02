package org.burningokr.service.exceptions;

public class InvalidEmailAddressException extends RuntimeException {
  public InvalidEmailAddressException(String errorMessage) {
    super(errorMessage);
  }
}

package org.burningokr.service.security;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(String message) {
    super(message);
  }
}

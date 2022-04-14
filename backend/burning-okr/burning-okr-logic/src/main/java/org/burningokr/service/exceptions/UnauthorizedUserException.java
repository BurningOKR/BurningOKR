package org.burningokr.service.exceptions;

public class UnauthorizedUserException extends RuntimeException{
  public UnauthorizedUserException(String message) {
    super(message);
  }
}

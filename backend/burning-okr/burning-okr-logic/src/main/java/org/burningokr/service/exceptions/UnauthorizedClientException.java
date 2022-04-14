package org.burningokr.service.exceptions;

public class UnauthorizedClientException extends RuntimeException{
  public UnauthorizedClientException(String message) {
    super(message);
  }
}

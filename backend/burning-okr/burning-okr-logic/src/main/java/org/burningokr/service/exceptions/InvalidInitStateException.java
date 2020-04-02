package org.burningokr.service.exceptions;

public class InvalidInitStateException extends RuntimeException {
  public InvalidInitStateException(String message) {
    super(message);
  }
}

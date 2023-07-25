package org.burningokr.service.exceptions;

public class GetterNotFoundException extends RuntimeException {
  public GetterNotFoundException(String message) {
    super(message);
  }
  public GetterNotFoundException(String message, Exception inner) {
    super(message, inner);
  }
}

package org.burningokr.controller.exceptions;

public class PutIdConflictException extends Exception {

  public PutIdConflictException(String errorMessage) {
    super(errorMessage);
  }
}

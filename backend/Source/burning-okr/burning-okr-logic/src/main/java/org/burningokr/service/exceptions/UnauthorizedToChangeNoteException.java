package org.burningokr.service.exceptions;

public class UnauthorizedToChangeNoteException extends RuntimeException {
  public UnauthorizedToChangeNoteException(String error) {
    super(error);
  }
}

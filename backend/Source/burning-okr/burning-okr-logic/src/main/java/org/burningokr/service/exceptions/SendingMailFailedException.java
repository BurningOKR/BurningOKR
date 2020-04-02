package org.burningokr.service.exceptions;

public class SendingMailFailedException extends RuntimeException {
  public SendingMailFailedException(String error) {
    super(error);
  }
}

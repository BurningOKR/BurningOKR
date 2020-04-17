package org.burningokr.service.exceptions;

public class DuplicateTeamMemberException extends RuntimeException {
  public DuplicateTeamMemberException(String message) {
    super(message);
  }
}

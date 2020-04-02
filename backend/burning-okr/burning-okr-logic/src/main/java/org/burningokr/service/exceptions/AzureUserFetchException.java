package org.burningokr.service.exceptions;

public class AzureUserFetchException extends Exception {
  public AzureUserFetchException(String error) {
    super(error);
  }
}

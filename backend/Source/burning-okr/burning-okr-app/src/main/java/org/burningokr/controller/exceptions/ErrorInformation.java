package org.burningokr.controller.exceptions;

import java.util.UUID;

public class ErrorInformation {

  private String errorInformation;
  private String errorId;

  public ErrorInformation(String errorInformation) {
    this.errorInformation = errorInformation;
    this.errorId = UUID.randomUUID().toString(); // fürs nachschauen im Log
  }

  // region Getter
  public String getErrorInformation() {
    return errorInformation;
  }

  public String getErrorId() {
    return errorId;
  }
  // endregion

}

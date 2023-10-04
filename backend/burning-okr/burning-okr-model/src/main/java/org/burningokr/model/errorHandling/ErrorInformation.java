package org.burningokr.model.errorHandling;

import lombok.Data;

import java.util.UUID;

@Data
public class ErrorInformation {
  private String errorInformation;
  private String errorId;

  public ErrorInformation(String errorInformation) {
    this.errorInformation = errorInformation;
    this.errorId = UUID.randomUUID().toString(); // for looking up in log files
  }
}

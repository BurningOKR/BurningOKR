package org.burningokr.model.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FrontendLogLevel {
  TRACE(0),
  DEBUG(1),
  INFO(2),
  LOG(3),
  WARN(4),
  ERROR(5),
  FATAL(6),
  OFF(7);

  private final int levelId;

  /**
   * Get the FrontendLogLevel with the given identifier.
   *
   * <p>The given LevelIdentifiert will be parsed as an integer and checked against the
   * FrontendLogLevel enum.
   *
   * @param levelIdentifier a string value
   * @return the FrontendLogLevel or null if LogLevel not found
   */
  public static FrontendLogLevel getLevelByIdentifier(String levelIdentifier) {
    FrontendLogLevel frontendLogLevel = null;

    for (FrontendLogLevel logLevel : FrontendLogLevel.values()) {
      if (logLevel.getLevelId() == Integer.parseInt(levelIdentifier)) {
        frontendLogLevel = logLevel;
        break;
      }
    }

    return frontendLogLevel;
  }
}

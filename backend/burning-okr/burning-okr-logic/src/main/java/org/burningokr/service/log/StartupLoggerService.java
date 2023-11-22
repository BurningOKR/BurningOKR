package org.burningokr.service.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StartupLoggerService {
  private final BuildProperties buildProperties;

  @EventListener(ApplicationStartedEvent.class)
  public void logReadyUp() {
    String burning_okr_message =
      """


          ____                   _                ____  _  _______ \s
         |  _ \\                 (_)              / __ \\| |/ /  __ \\\s
         | |_) |_   _ _ __ _ __  _ _ __   __ _  | |  | | ' /| |__) |
         |  _ <| | | | '__| '_ \\| | '_ \\ / _` | | |  | |  < |  _  /\s
         | |_) | |_| | |  | | | | | | | | (_| | | |__| | . \\| | \\ \\\s
         |____/ \\__,_|_|  |_| |_|_|_| |_|\\__, |  \\____/|_|\\_\\_|  \\_\\
                                          __/ |                    \s
                                         |___/                     \s
        Version: v{}""";
    log.info(burning_okr_message, buildProperties.getVersion());
  }
}

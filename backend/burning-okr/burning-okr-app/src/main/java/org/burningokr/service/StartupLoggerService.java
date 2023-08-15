package org.burningokr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StartupLoggerService {
  @Autowired
  private BuildProperties buildProperties;

  @EventListener(ApplicationStartedEvent.class)
  public void logReadyUp() {
    String burning_okr_message =
      new StringBuilder()
        .append("\n")
        .append("\n")
        .append("  ____                   _                ____  _  _______  \n")
        .append(" |  _ \\                 (_)              / __ \\| |/ /  __ \\ \n")
        .append(" | |_) |_   _ _ __ _ __  _ _ __   __ _  | |  | | ' /| |__) |\n")
        .append(" |  _ <| | | | '__| '_ \\| | '_ \\ / _` | | |  | |  < |  _  / \n")
        .append(" | |_) | |_| | |  | | | | | | | | (_| | | |__| | . \\| | \\ \\ \n")
        .append(" |____/ \\__,_|_|  |_| |_|_|_| |_|\\__, |  \\____/|_|\\_\\_|  \\_\\\n")
        .append("                                  __/ |                     \n")
        .append("                                 |___/                      \n")
        .append("Version: v{}")
        .toString();
    log.info(burning_okr_message, buildProperties.getVersion());
  }
}

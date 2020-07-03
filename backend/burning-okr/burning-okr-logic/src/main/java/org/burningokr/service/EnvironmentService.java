package org.burningokr.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentService {
  private ApplicationContext applicationContext;

  public static final String authMode = "system.configuration.auth-mode";
  public static final String authModeAad = "aad";
  public static final String authModeLocal = "local";
  public static final String authModeDemo = "demo";

  public EnvironmentService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public String getProperty(String propertyName) {
    return applicationContext.getEnvironment().getProperty(propertyName);
  }
}

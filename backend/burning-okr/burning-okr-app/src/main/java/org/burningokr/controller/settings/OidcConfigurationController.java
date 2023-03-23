package org.burningokr.controller.settings;

import lombok.AllArgsConstructor;
import org.burningokr.config.SystemProperties;
import org.burningokr.model.settings.OidcConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applicationSettings/oidcConfiguration")
@AllArgsConstructor
public class OidcConfigurationController {
  private final SystemProperties systemProperties;

  @GetMapping
  public OidcConfiguration getOidcConfig() {
    return OidcConfiguration.builder()
      .clientId(systemProperties.getClientId())
      .accessTokenUrl(systemProperties.getAccessTokenUrl())
      .authUrl(systemProperties.getAuthUrl())
      .build();
  }
}

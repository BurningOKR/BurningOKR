package org.burningokr.controller.settings;

import lombok.AllArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.model.settings.OidcConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestApiController
@RequestMapping("/applicationSettings/oidcConfiguration")
@AllArgsConstructor
public class OidcConfigurationController {
  private final SystemProperties systemProperties;

  @GetMapping
  public OidcConfiguration getOidcConfig() {
    return OidcConfiguration.builder()
      .clientId(systemProperties.getClientId())
      .issuerUri(systemProperties.getIssuerUri())
      .scopes(systemProperties.getScopes())
      .strictDiscoveryDocumentValidation(systemProperties.isStrictDiscoveryDocumentValidation())
      .build();
  }
}

package org.burningokr.applicationlisteners;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.burningokr.service.condition.LocalUserCondition;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Conditional(LocalUserCondition.class)
@Component
@RequiredArgsConstructor
public class ApplicationStartupLocalAuthConfigurer {
  
  private final Environment environment;
  private final OAuthConfigurationService oAuthConfigurationService;

  private final String defaultTokenEndpoint = "/oauth/token";

  /**
   * This function adds the token endpoint prefix to the token endpoint setting in the database.
   *
   * @param event The ApplicationReadyEvent
   */
  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationEvent(ApplicationReadyEvent event) {
    String tokenEndpointPrefix = environment.getProperty("system.configuration.token-endpoint-prefix");
    oAuthConfigurationService.setOAuthConfiguration(OAuthConfigurationName.TOKEN_ENDPOINT, tokenEndpointPrefix + defaultTokenEndpoint);
  }
}

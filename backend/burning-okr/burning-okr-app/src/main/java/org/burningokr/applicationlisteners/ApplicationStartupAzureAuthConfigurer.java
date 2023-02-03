package org.burningokr.applicationlisteners;

import lombok.RequiredArgsConstructor;
import org.burningokr.consts.DefaultAzureAuthData;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.burningokr.service.userutil.AzureAdProperties;
import org.burningokr.service.userutil.ExternalOAuthClientDetails;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Conditional(AadCondition.class)
@Component
@RequiredArgsConstructor
public class ApplicationStartupAzureAuthConfigurer {

  private final OAuthConfigurationService oAuthConfigurationService;
  private final ExternalOAuthClientDetails externalOAuthClientDetails;
  private final AzureAdProperties azureAdProperties;

  /**
   * This function moves the required configurations from the application.yml to the
   * OAuthConfiguration Database.
   *
   * @param event The ApplicationReadyEvent
   */
  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationEvent(ApplicationReadyEvent event) {
    oAuthConfigurationService.updateOAuthConfiguration(externalOAuthClientDetails);
    oAuthConfigurationService.setOAuthConfiguration(
      OAuthConfigurationName.ISSUER, azureAdProperties.getIssuer());
    oAuthConfigurationService.setOAuthConfiguration(
      OAuthConfigurationName.RESPONSE_TYPE, DefaultAzureAuthData.responseType);
    oAuthConfigurationService.setOAuthConfiguration(
      OAuthConfigurationName.OIDC, DefaultAzureAuthData.oidc);
    oAuthConfigurationService.setOAuthConfiguration(
      OAuthConfigurationName.AUTH_TYPE, DefaultAzureAuthData.authType);
  }
}

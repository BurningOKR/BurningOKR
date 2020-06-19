package org.burningokr.applicationlisteners;

import lombok.RequiredArgsConstructor;
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
public class ApplicationStartupAzureConfigurer {

  private final OAuthConfigurationService oAuthConfigurationService;
  private final ExternalOAuthClientDetails externalOAuthClientDetails;
  private final AzureAdProperties azureAdProperties;

  private final String defaultResponseType = "id_token token";
  private final String defaultOIDC = "true";
  private final String azureAuthType = "azure";

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
        OAuthConfigurationName.RESPONSE_TYPE, defaultResponseType);
    oAuthConfigurationService.setOAuthConfiguration(OAuthConfigurationName.OIDC, defaultOIDC);
    oAuthConfigurationService.setOAuthConfiguration(
        OAuthConfigurationName.AUTH_TYPE, azureAuthType);
  }
}

package org.burningokr.applicationlisteners;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.burningokr.model.initialisation.InitState;
import org.burningokr.model.initialisation.InitStateName;
import org.burningokr.service.configuration.OAuthClientDetailsService;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.burningokr.service.environment.AuthModes;
import org.burningokr.service.initialisation.InitService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartupDemoConfigurer {

  private final OAuthConfigurationService oAuthConfigurationService;
  private final OAuthClientDetailsService oauthClientDetailsService;
  private final InitService initService;

  @EventListener(ApplicationReadyEvent.class)
  @Order(2)
  public void onApplicationEvent(ApplicationReadyEvent event) {
    oAuthConfigurationService.setOAuthConfiguration(
        OAuthConfigurationName.AUTH_TYPE, AuthModes.DEMO.getName());

    OAuthClientDetails oAuthClientDetails = new OAuthClientDetails();
    oAuthClientDetails.setAccessTokenValidity(Integer.MAX_VALUE);
    oAuthClientDetails.setRefreshTokenValidity(Integer.MAX_VALUE);
    oAuthClientDetails.setClientId(
        UUID.randomUUID()
            .toString()); // R.J. 03.08.20: We are using a random UUID as a random string here.
    oAuthClientDetails.setClientSecret(
        UUID.randomUUID()
            .toString()); // R.J. 03.08.20: We are using a random UUID as a random string here.

    oauthClientDetailsService.fillDefaultValues(oAuthClientDetails);
    oAuthConfigurationService.updateOAuthConfiguration(oAuthClientDetails);
    oauthClientDetailsService.encodeClientSecret(oAuthClientDetails);
    oauthClientDetailsService.updateOAuthClientDetails(oAuthClientDetails);

    InitState initState = initService.getInitState();
    initState.setInitState(InitStateName.INITIALIZED);
    initService.saveInitState(initState);
  }
}

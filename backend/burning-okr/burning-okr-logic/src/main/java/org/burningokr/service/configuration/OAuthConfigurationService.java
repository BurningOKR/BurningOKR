package org.burningokr.service.configuration;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.configuration.OAuthConfiguration;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.burningokr.repositories.configuration.OAuthConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthConfigurationService {

  private final OAuthConfigurationRepository oAuthConfigurationRepository;

  /** Gets the OAuthConfigurations. */
  public Collection<OAuthConfiguration> getOAuthConfigurations() {
    return Lists.newArrayList(oAuthConfigurationRepository.findAll());
  }

  public OAuthConfiguration getOAuthConfigurationByName(OAuthConfigurationName name) {
    return getConfigurationByName(getOAuthConfigurations(), name);
  }

  /**
   * Updates the OAuthConfiguration to match the given OauthClientDetails.
   *
   * @param oauthClientDetails a {@link OAuthClientDetails} object
   */
  public void updateOAuthConfiguration(OAuthClientDetails oauthClientDetails) {
    Collection<OAuthConfiguration> oAuthConfigurations = getOAuthConfigurations();

    OAuthConfiguration clientId = getConfigurationByName(oAuthConfigurations, OAuthConfigurationName.CLIENT_ID);
    OAuthConfiguration clientSecret = getConfigurationByName(oAuthConfigurations, OAuthConfigurationName.CLIENT_SECRET);
    OAuthConfiguration redirectUri = getConfigurationByName(oAuthConfigurations, OAuthConfigurationName.REDIRECT_URI);

    clientId.setValue(oauthClientDetails.getClientId());
    clientSecret.setValue(oauthClientDetails.getClientSecret());
    redirectUri.setValue(oauthClientDetails.getWebServerRedirectUri());

    oAuthConfigurationRepository.save(clientId);
    oAuthConfigurationRepository.save(clientSecret);
    oAuthConfigurationRepository.save(redirectUri);
  }

  private OAuthConfiguration getConfigurationByName(Collection<OAuthConfiguration> configurations, OAuthConfigurationName configurationName) {
    Optional<OAuthConfiguration> foundConfiguration = configurations.stream()
        .filter(configuration -> configuration.getKey().equals(configurationName.getName()))
        .findFirst();

    return foundConfiguration.orElse(new OAuthConfiguration(configurationName, "", "text"));
  }
}

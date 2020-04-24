package org.burningokr.service.configuration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;
import org.burningokr.model.configuration.ConfigurationType;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.configuration.OAuthConfiguration;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.burningokr.repositories.configuration.OAuthConfigurationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OAuthConfigurationServiceTest {

  @Mock private OAuthConfigurationRepository oAuthConfigurationRepository;

  @InjectMocks private OAuthConfigurationService oAuthConfigurationService;

  @Test
  public void updateOauthFrontendDetails_updates_details() {

    when(oAuthConfigurationRepository.findAll()).thenReturn(Collections.emptyList());

    OAuthClientDetails oAuthClientDetails = new OAuthClientDetails();
    oAuthClientDetails.setClientId("test123");
    oAuthClientDetails.setClientSecret("test456");
    oAuthClientDetails.setWebServerRedirectUri("www.example.com");

    oAuthConfigurationService.updateOAuthConfiguration(oAuthClientDetails);

    verify(oAuthConfigurationRepository, times(3)).save(any());
  }

  @Test
  public void getOauthFrontendDetails_getsDetails() {

    OAuthConfiguration clientId =
        new OAuthConfiguration(OAuthConfigurationName.CLIENT_ID, "test123", ConfigurationType.TEXT);
    OAuthConfiguration clientSecret =
        new OAuthConfiguration(
            OAuthConfigurationName.CLIENT_SECRET, "abc123", ConfigurationType.TEXT);

    when(oAuthConfigurationRepository.findAll()).thenReturn(Arrays.asList(clientId, clientSecret));

    Collection<OAuthConfiguration> oAuthConfigurations =
        oAuthConfigurationService.getOAuthConfigurations();

    assertEquals(
        "test123",
        getConfigurationByName(oAuthConfigurations, OAuthConfigurationName.CLIENT_ID).getValue());
    assertEquals(
        "abc123",
        getConfigurationByName(oAuthConfigurations, OAuthConfigurationName.CLIENT_SECRET)
            .getValue());
  }

  private OAuthConfiguration getConfigurationByName(
      Collection<OAuthConfiguration> configurations, OAuthConfigurationName configurationName) {
    Optional<OAuthConfiguration> foundConfiguration =
        configurations.stream()
            .filter(configuration -> configuration.getKey().equals(configurationName.getName()))
            .findFirst();

    return foundConfiguration.orElse(
        new OAuthConfiguration(configurationName, "", ConfigurationType.TEXT));
  }
}

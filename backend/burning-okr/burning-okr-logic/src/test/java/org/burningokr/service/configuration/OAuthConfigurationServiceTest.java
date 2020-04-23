package org.burningokr.service.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.model.configuration.OAuthFrontendDetails;
import org.burningokr.repositories.configuration.OAuthFrontendDetailsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OAuthConfigurationServiceTest {

  @Mock private OAuthFrontendDetailsRepository oAuthFrontendDetailsRepository;

  @InjectMocks private OAuthConfigurationService oAuthConfigurationService;

  OAuthFrontendDetails oAuthFrontendDetails;

  @Before
  public void init() {
    oAuthFrontendDetails = new OAuthFrontendDetails();
  }

  @Test
  public void updateOauthFrontendDetails_updates_details() {

    when(oAuthFrontendDetailsRepository.findAll())
        .thenReturn(Collections.singletonList(oAuthFrontendDetails));

    OAuthClientDetails oAuthClientDetails = new OAuthClientDetails();
    oAuthClientDetails.setClientId("test123");
    oAuthClientDetails.setClientSecret("test456");
    oAuthClientDetails.setWebServerRedirectUri("www.example.com");

    oAuthConfigurationService.updateOAuthConfiguration(oAuthClientDetails);

    assertEquals(oAuthClientDetails.getClientId(), oAuthFrontendDetails.getClientId());
    assertEquals(oAuthClientDetails.getClientSecret(), oAuthFrontendDetails.getDummyClientSecret());
    assertEquals(
        oAuthClientDetails.getWebServerRedirectUri(), oAuthClientDetails.getWebServerRedirectUri());
  }

  @Test
  public void getOauthFrontendDetails_getsDetails() {

    oAuthFrontendDetails.setClientId("test");
    oAuthFrontendDetails.setDummyClientSecret("test123");

    when(oAuthFrontendDetailsRepository.findAll())
        .thenReturn(Collections.singletonList(oAuthFrontendDetails));

    OAuthFrontendDetails oAuthFrontendDetailsFromService =
        oAuthConfigurationService.getOAuthConfigurations();

    assertEquals(oAuthFrontendDetails.getClientId(), oAuthFrontendDetailsFromService.getClientId());
    assertEquals(
        oAuthFrontendDetails.getDummyClientSecret(),
        oAuthFrontendDetailsFromService.getDummyClientSecret());
  }

  @Test
  public void getOauthFrontendDetails_getsOnlyFirstDetails() {

    oAuthFrontendDetails.setClientId("test");
    oAuthFrontendDetails.setDummyClientSecret("test123");

    OAuthFrontendDetails oAuthFrontendDetails2 = new OAuthFrontendDetails();
    oAuthFrontendDetails2.setClientId("test2");
    oAuthFrontendDetails2.setDummyClientSecret("test456");

    when(oAuthFrontendDetailsRepository.findAll())
        .thenReturn(Arrays.asList(oAuthFrontendDetails, oAuthFrontendDetails2));

    OAuthFrontendDetails oAuthFrontendDetailsFromService =
        oAuthConfigurationService.getOAuthConfigurations();

    assertEquals(oAuthFrontendDetails.getClientId(), oAuthFrontendDetailsFromService.getClientId());
    assertEquals(
        oAuthFrontendDetails.getDummyClientSecret(),
        oAuthFrontendDetailsFromService.getDummyClientSecret());
  }

  @Test
  public void getOauthFrontendDetails_throwsExceptionWhenEmpty() {

    when(oAuthFrontendDetailsRepository.findAll()).thenReturn(Collections.emptyList());

    try {
      oAuthConfigurationService.getOAuthConfigurations();
      fail();
    } catch (Exception e) {
      assertTrue("Wrong Exception", e instanceof EntityNotFoundException);
    }
  }
}

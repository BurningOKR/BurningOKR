package org.burningokr.service.configuration;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.repositories.configuration.OAuthClientDetailsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OAuthClientDetailsServiceTest {

  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private OAuthClientDetailsRepository oauthClientDetailsRepository;

  @InjectMocks
  private OAuthClientDetailsService oauthClientDetailsService;

  private OAuthClientDetails oauthClientDetails;

  @Before
  public void init() {
    oauthClientDetails = new OAuthClientDetails();

    when(passwordEncoder.encode(any())).thenReturn("Encoded String");
  }

  @Test
  public void fillDefaultValues_fillsValues() {
    oauthClientDetailsService.fillDefaultValues(oauthClientDetails);

    assertEquals("oauth2-resource", oauthClientDetails.getResourceIds());
    assertEquals("USER", oauthClientDetails.getScope());
    assertEquals("password,refresh_token", oauthClientDetails.getAuthorizedGrantTypes());
    assertEquals("", oauthClientDetails.getAuthorities());
    assertEquals("{}", oauthClientDetails.getAdditionalInformation());
    assertEquals("", oauthClientDetails.getAutoapprove());
  }

  @Test
  public void encodeClientSecret_encodesSecret() {
    oauthClientDetails.setClientSecret("Test123");

    oauthClientDetailsService.encodeClientSecret(oauthClientDetails);

    assertEquals("Encoded String", oauthClientDetails.getClientSecret());
  }

  @Test
  public void getOAuthClientDetails_expectOAuthClientDetails() {
    oauthClientDetails.setClientId("test");
    oauthClientDetails.setClientSecret("testSecret");
    oauthClientDetails.setAutoapprove("testApprove");
    oauthClientDetails.setAdditionalInformation("testInformation");
    oauthClientDetails.setAuthorities("testAuthorities");
    oauthClientDetails.setAuthorizedGrantTypes("testGrants");
    oauthClientDetails.setScope("testScope");
    oauthClientDetails.setResourceIds("testResources");
    oauthClientDetails.setWebServerRedirectUri("testUri");
    oauthClientDetails.setAccessTokenValidity(50);
    oauthClientDetails.setRefreshTokenValidity(50);
    Iterable<OAuthClientDetails> iterable = Collections.singletonList(oauthClientDetails);

    when(oauthClientDetailsRepository.findAll()).thenReturn(iterable);

    OAuthClientDetails oauthClientDetailsFromService =
      oauthClientDetailsService.getOAuthClientDetails();

    verify(oauthClientDetailsRepository).findAll();

    assertEquals(oauthClientDetails.getClientId(), oauthClientDetails.getClientId());
    assertEquals(
      oauthClientDetails.getRefreshTokenValidity(), oauthClientDetails.getRefreshTokenValidity());
    assertEquals(
      oauthClientDetails.getAccessTokenValidity(), oauthClientDetails.getAccessTokenValidity());
    assertEquals(oauthClientDetails.getClientSecret(), oauthClientDetails.getClientSecret());
    assertEquals(
      oauthClientDetails.getAdditionalInformation(),
      oauthClientDetails.getAdditionalInformation()
    );
    assertEquals(oauthClientDetails.getAuthorities(), oauthClientDetails.getAuthorities());
    assertEquals(
      oauthClientDetails.getAuthorizedGrantTypes(), oauthClientDetails.getAuthorizedGrantTypes());
    assertEquals(oauthClientDetails.getAutoapprove(), oauthClientDetails.getAutoapprove());
    assertEquals(oauthClientDetails.getResourceIds(), oauthClientDetails.getResourceIds());
    assertEquals(oauthClientDetails.getScope(), oauthClientDetails.getScope());
    assertEquals(
      oauthClientDetails.getWebServerRedirectUri(), oauthClientDetails.getWebServerRedirectUri());
  }

  @Test
  public void getOAuthClientDetails_expectException() {
    Iterable<OAuthClientDetails> iterable = Collections.emptyList();

    when(oauthClientDetailsRepository.findAll()).thenReturn(iterable);

    try {
      oauthClientDetailsService.getOAuthClientDetails();
      fail(); // Should throw exception before getting here
    } catch (Exception e) {
      assertTrue(e instanceof EntityNotFoundException);
    }
  }

  @Test
  public void updateOAuthClientDetails_setsOAuthClientDetails() {
    oauthClientDetailsService.updateOAuthClientDetails(oauthClientDetails);

    verify(oauthClientDetailsRepository).deleteAll();
    verify(oauthClientDetailsRepository).save(oauthClientDetails);
  }
}

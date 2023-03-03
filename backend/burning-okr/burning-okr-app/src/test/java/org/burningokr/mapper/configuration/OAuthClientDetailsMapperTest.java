package org.burningokr.mapper.configuration;

import org.burningokr.dto.configuration.OAuthClientDetailsDto;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OAuthClientDetailsMapperTest {

  private OAuthClientDetails oauthClientDetails;
  private OAuthClientDetailsDto oauthClientDetailsDto;
  private OAuthClientDetailsMapper oauthClientDetailsMapper;

  @BeforeEach
  public void init() {
    oauthClientDetailsDto = new OAuthClientDetailsDto();
    oauthClientDetails = new OAuthClientDetails();

    this.oauthClientDetailsMapper = new OAuthClientDetailsMapper();
  }

  @Test
  public void mapDtoToEntity_expectAllMapped() {
    oauthClientDetailsDto.setClientId("Test123");
    oauthClientDetailsDto.setClientSecret("TestABC");
    oauthClientDetailsDto.setRefreshTokenValidity(10);
    oauthClientDetailsDto.setAccessTokenValidity(10);
    oauthClientDetailsDto.setWebServerRedirectUri("localhost");

    oauthClientDetails = oauthClientDetailsMapper.mapDtoToEntity(oauthClientDetailsDto);

    assertEquals(oauthClientDetailsDto.getClientId(), oauthClientDetails.getClientId());
    assertEquals(oauthClientDetailsDto.getClientSecret(), oauthClientDetails.getClientSecret());
    assertEquals(
      oauthClientDetailsDto.getRefreshTokenValidity(),
      oauthClientDetails.getRefreshTokenValidity()
    );
    assertEquals(
      oauthClientDetailsDto.getAccessTokenValidity(),
      oauthClientDetails.getAccessTokenValidity()
    );
    assertEquals(
      oauthClientDetailsDto.getWebServerRedirectUri(),
      oauthClientDetails.getWebServerRedirectUri()
    );
  }

  @Test
  public void mapEntityToDto_expectAllMapped() {
    oauthClientDetails.setClientId("Test123");
    oauthClientDetails.setClientSecret("TestABC");
    oauthClientDetails.setRefreshTokenValidity(10);
    oauthClientDetails.setAccessTokenValidity(10);
    oauthClientDetails.setWebServerRedirectUri("localhost");

    oauthClientDetails.setAdditionalInformation("Hallo 123");
    oauthClientDetails.setAuthorities("Authorities");
    oauthClientDetails.setAuthorizedGrantTypes("Grant Types");
    oauthClientDetails.setAutoapprove("OKR Tool Approves");
    oauthClientDetails.setResourceIds("Resource Ids");
    oauthClientDetails.setScope("scope");

    oauthClientDetailsDto = oauthClientDetailsMapper.mapEntityToDto(oauthClientDetails);

    assertEquals(oauthClientDetails.getClientId(), oauthClientDetailsDto.getClientId());
    assertEquals(oauthClientDetails.getClientSecret(), oauthClientDetailsDto.getClientSecret());
    assertEquals(
      oauthClientDetails.getRefreshTokenValidity(),
      oauthClientDetailsDto.getRefreshTokenValidity()
    );
    assertEquals(
      oauthClientDetails.getAccessTokenValidity(),
      oauthClientDetailsDto.getAccessTokenValidity()
    );
    assertEquals(
      oauthClientDetails.getWebServerRedirectUri(),
      oauthClientDetailsDto.getWebServerRedirectUri()
    );
  }

  @Test
  public void mapEntitiesToDtos_expectEmptyList() {
    Collection<OAuthClientDetails> oauthClientDetailsCollection = new ArrayList<>();

    Collection<OAuthClientDetailsDto> oauthClientDetailsDtoCollection =
      oauthClientDetailsMapper.mapEntitiesToDtos(oauthClientDetailsCollection);

    assertTrue(oauthClientDetailsDtoCollection.isEmpty());
  }

  @Test
  public void mapEntitiesToDtos_expectAllMapped() {
    OAuthClientDetails oauthClientDetails1 = new OAuthClientDetails();
    oauthClientDetails1.setClientId("Test123");
    oauthClientDetails1.setClientSecret("TestABC");
    oauthClientDetails1.setRefreshTokenValidity(10);
    oauthClientDetails1.setAccessTokenValidity(10);
    oauthClientDetails1.setWebServerRedirectUri("localhost");

    oauthClientDetails1.setAdditionalInformation("Hallo 123");
    oauthClientDetails1.setAuthorities("Authorities");
    oauthClientDetails1.setAuthorizedGrantTypes("Grant Types");
    oauthClientDetails1.setAutoapprove("OKR Tool Approves");
    oauthClientDetails1.setResourceIds("Resource Ids");
    oauthClientDetails1.setScope("scope");

    OAuthClientDetails oauthClientDetails2 = new OAuthClientDetails();
    oauthClientDetails2.setClientId("Test456");
    oauthClientDetails2.setClientSecret("TestDEF");
    oauthClientDetails2.setRefreshTokenValidity(20);
    oauthClientDetails2.setAccessTokenValidity(20);
    oauthClientDetails2.setWebServerRedirectUri("google.com");

    oauthClientDetails2.setAdditionalInformation("Hallo 123");
    oauthClientDetails2.setAuthorities("Authorities");
    oauthClientDetails2.setAuthorizedGrantTypes("Grant Types");
    oauthClientDetails2.setAutoapprove("OKR Tool Approves");
    oauthClientDetails2.setResourceIds("Resource Ids");
    oauthClientDetails2.setScope("scope");

    Collection<OAuthClientDetails> oauthClientDetailsCollection =
      Arrays.asList(oauthClientDetails1, oauthClientDetails2);

    Collection<OAuthClientDetailsDto> oauthClientDetailsDtoCollection =
      oauthClientDetailsMapper.mapEntitiesToDtos(oauthClientDetailsCollection);

    assertEquals(oauthClientDetailsCollection.size(), oauthClientDetailsDtoCollection.size());
    Optional<OAuthClientDetailsDto> firstOAuthClientDetailsDto =
      oauthClientDetailsDtoCollection.stream().findFirst();
    Optional<OAuthClientDetailsDto> secondOAuthClientDetailsDto =
      oauthClientDetailsDtoCollection.stream().skip(1).findFirst();
    if (firstOAuthClientDetailsDto.isPresent() && secondOAuthClientDetailsDto.isPresent()) {
      assertEquals(
        oauthClientDetails1.getClientId(), firstOAuthClientDetailsDto.get().getClientId());
      assertEquals(
        oauthClientDetails1.getClientSecret(),
        firstOAuthClientDetailsDto.get().getClientSecret()
      );
      assertEquals(
        oauthClientDetails1.getAccessTokenValidity(),
        firstOAuthClientDetailsDto.get().getAccessTokenValidity()
      );
      assertEquals(
        oauthClientDetails1.getRefreshTokenValidity(),
        firstOAuthClientDetailsDto.get().getRefreshTokenValidity()
      );
    } else {
      fail();
    }
  }

  @Test
  public void mapDtosToEntities_expectEmptyList() {
    Collection<OAuthClientDetailsDto> oauthClientDetailsDtoCollection = new ArrayList<>();

    Collection<OAuthClientDetails> oauthClientDetailsCollection =
      oauthClientDetailsMapper.mapDtosToEntities(oauthClientDetailsDtoCollection);

    assertTrue(oauthClientDetailsCollection.isEmpty());
  }

  @Test
  public void mapDtosToEntities_expectAllMapped() {
    OAuthClientDetailsDto oauthClientDetailsDto1 = new OAuthClientDetailsDto();
    oauthClientDetailsDto1.setClientId("Test123");
    oauthClientDetailsDto1.setClientSecret("TestABC");
    oauthClientDetailsDto1.setRefreshTokenValidity(10);
    oauthClientDetailsDto1.setAccessTokenValidity(10);
    oauthClientDetailsDto1.setWebServerRedirectUri("localhost");

    OAuthClientDetailsDto oauthClientDetailsDto2 = new OAuthClientDetailsDto();
    oauthClientDetailsDto2.setClientId("Test456");
    oauthClientDetailsDto2.setClientSecret("TestDEF");
    oauthClientDetailsDto2.setRefreshTokenValidity(20);
    oauthClientDetailsDto2.setAccessTokenValidity(20);
    oauthClientDetailsDto2.setWebServerRedirectUri("google.com");

    Collection<OAuthClientDetailsDto> oauthClientDetailsDtoCollection =
      Arrays.asList(oauthClientDetailsDto1, oauthClientDetailsDto2);

    Collection<OAuthClientDetails> oauthClientDetailsCollection =
      oauthClientDetailsMapper.mapDtosToEntities(oauthClientDetailsDtoCollection);

    assertEquals(oauthClientDetailsDtoCollection.size(), oauthClientDetailsCollection.size());
    Optional<OAuthClientDetails> firstOAuthClientDetails =
      oauthClientDetailsCollection.stream().findFirst();
    Optional<OAuthClientDetails> secondOAuthClientDetails =
      oauthClientDetailsCollection.stream().skip(1).findFirst();
    if (firstOAuthClientDetails.isPresent() && secondOAuthClientDetails.isPresent()) {
      assertEquals(
        oauthClientDetailsDto1.getClientId(), firstOAuthClientDetails.get().getClientId());
      assertEquals(
        oauthClientDetailsDto1.getClientSecret(),
        firstOAuthClientDetails.get().getClientSecret()
      );
      assertEquals(
        oauthClientDetailsDto1.getAccessTokenValidity(),
        firstOAuthClientDetails.get().getAccessTokenValidity()
      );
      assertEquals(
        oauthClientDetailsDto1.getRefreshTokenValidity(),
        firstOAuthClientDetails.get().getRefreshTokenValidity()
      );
    } else {
      fail();
    }
  }
}

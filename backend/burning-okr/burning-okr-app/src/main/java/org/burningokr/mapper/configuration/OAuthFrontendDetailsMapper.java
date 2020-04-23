package org.burningokr.mapper.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.burningokr.dto.configuration.OAuthFrontendDetailsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthConfiguration;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.springframework.stereotype.Service;

@Service
public class OAuthFrontendDetailsMapper
    implements DataMapper<Collection<OAuthConfiguration>, OAuthFrontendDetailsDto> {

  @Override
  public Collection<OAuthConfiguration> mapDtoToEntity(OAuthFrontendDetailsDto input) {
    Collection<OAuthConfiguration> configurations = new ArrayList<>();
    configurations.add(
        new OAuthConfiguration(OAuthConfigurationName.CLIENT_ID, input.getClientId(), "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.CLIENT_SECRET, input.getDummyClientSecret(), "text"));
    configurations.add(
        new OAuthConfiguration(OAuthConfigurationName.AUTH_TYPE, input.getAuthType(), "text"));
    configurations.add(
        new OAuthConfiguration(OAuthConfigurationName.ISSUER, input.getIssuer(), "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.REDIRECT_URI, input.getRedirectUri(), "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.RESPONSE_TYPE, input.getResponseType(), "text"));
    configurations.add(
        new OAuthConfiguration(OAuthConfigurationName.SCOPE, input.getScope(), "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.SILENT_REFRESH_REDIRECT_URI,
            input.getSilentRefreshRedirectUri(),
            "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.TOKEN_ENDPOINT, input.getTokenEndpoint(), "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.OIDC, booleanToString(input.getOidc()), "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.REQUIRE_HTTPS,
            booleanToString(input.getRequireHttps()),
            "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.SHOW_DEBUG_INFORMATION,
            booleanToString(input.getShowDebugInformation()),
            "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.USE_HTTP_BASIC_AUTH,
            booleanToString(input.getUseHttpBasicAuth()),
            "text"));
    configurations.add(
        new OAuthConfiguration(
            OAuthConfigurationName.STRICT_DISCOVERY_DOCUMENT_VALIDATION,
            booleanToString(input.getStrictDiscoveryDocumentValidation()),
            "text"));
    return configurations;
  }

  @Override
  public OAuthFrontendDetailsDto mapEntityToDto(Collection<OAuthConfiguration> input) {
    OAuthFrontendDetailsDto oauthFrontendDetails = new OAuthFrontendDetailsDto();

    oauthFrontendDetails.setClientId(
        getConfigurationByName(input, OAuthConfigurationName.CLIENT_ID).getValue());

    oauthFrontendDetails.setScope(
        getConfigurationByName(input, OAuthConfigurationName.SCOPE).getValue());

    oauthFrontendDetails.setRedirectUri(
        getConfigurationByName(input, OAuthConfigurationName.REDIRECT_URI).getValue());

    oauthFrontendDetails.setSilentRefreshRedirectUri(
        getConfigurationByName(input, OAuthConfigurationName.SILENT_REFRESH_REDIRECT_URI)
            .getValue());

    oauthFrontendDetails.setAuthType(
        getConfigurationByName(input, OAuthConfigurationName.AUTH_TYPE).getValue());

    oauthFrontendDetails.setIssuer(
        getConfigurationByName(input, OAuthConfigurationName.ISSUER).getValue());

    oauthFrontendDetails.setResponseType(
        getConfigurationByName(input, OAuthConfigurationName.RESPONSE_TYPE).getValue());

    oauthFrontendDetails.setTokenEndpoint(
        getConfigurationByName(input, OAuthConfigurationName.TOKEN_ENDPOINT).getValue());

    oauthFrontendDetails.setDummyClientSecret(
        getConfigurationByName(input, OAuthConfigurationName.CLIENT_SECRET).getValue());

    oauthFrontendDetails.setOidc(
        stringToBoolean(getConfigurationByName(input, OAuthConfigurationName.OIDC).getValue()));

    oauthFrontendDetails.setShowDebugInformation(
        stringToBoolean(
            getConfigurationByName(input, OAuthConfigurationName.SHOW_DEBUG_INFORMATION)
                .getValue()));

    oauthFrontendDetails.setRequireHttps(
        stringToBoolean(
            getConfigurationByName(input, OAuthConfigurationName.REQUIRE_HTTPS).getValue()));

    oauthFrontendDetails.setStrictDiscoveryDocumentValidation(
        stringToBoolean(
            getConfigurationByName(
                    input, OAuthConfigurationName.STRICT_DISCOVERY_DOCUMENT_VALIDATION)
                .getValue()));

    oauthFrontendDetails.setUseHttpBasicAuth(
        stringToBoolean(
            getConfigurationByName(input, OAuthConfigurationName.USE_HTTP_BASIC_AUTH).getValue()));

    return oauthFrontendDetails;
  }

  @Override
  public Collection<Collection<OAuthConfiguration>> mapDtosToEntities(
      Collection<OAuthFrontendDetailsDto> input) {
    return input.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
  }

  @Override
  public Collection<OAuthFrontendDetailsDto> mapEntitiesToDtos(
      Collection<Collection<OAuthConfiguration>> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }

  private OAuthConfiguration getConfigurationByName(
      Collection<OAuthConfiguration> configurations, OAuthConfigurationName configurationName) {
    Optional<OAuthConfiguration> foundConfiguration =
        configurations.stream()
            .filter(configuration -> configuration.getKey().equals(configurationName.getName()))
            .findFirst();

    return foundConfiguration.orElse(new OAuthConfiguration(configurationName, "", "text"));
  }

  private boolean stringToBoolean(String string) {
    return string.equals("true");
  }

  private String booleanToString(Boolean bool) {
    if (bool == null) return null;
    return bool ? "true" : "false";
  }
}

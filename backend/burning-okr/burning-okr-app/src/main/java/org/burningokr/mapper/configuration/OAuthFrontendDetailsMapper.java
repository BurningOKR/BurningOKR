package org.burningokr.mapper.configuration;

import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.dto.configuration.OAuthFrontendDetailsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthFrontendDetails;
import org.springframework.stereotype.Service;

@Service
public class OAuthFrontendDetailsMapper
    implements DataMapper<OAuthFrontendDetails, OAuthFrontendDetailsDto> {
  @Override
  public OAuthFrontendDetails mapDtoToEntity(OAuthFrontendDetailsDto input) {
    return new OAuthFrontendDetails();
  }

  @Override
  public OAuthFrontendDetailsDto mapEntityToDto(OAuthFrontendDetails input) {
    OAuthFrontendDetailsDto oauthFrontendDetails = new OAuthFrontendDetailsDto();

    oauthFrontendDetails.setClientId(input.getClientId());
    oauthFrontendDetails.setScope(input.getScope());
    oauthFrontendDetails.setRedirectUri(input.getRedirectUri());
    oauthFrontendDetails.setSilentRefreshRedirectUri(input.getSilentRefreshRedirectUri());
    oauthFrontendDetails.setAuthType(input.getAuthType());
    oauthFrontendDetails.setIssuer(input.getIssuer());
    oauthFrontendDetails.setResponseType(input.getResponseType());
    oauthFrontendDetails.setTokenEndpoint(input.getTokenEndpoint());
    oauthFrontendDetails.setDummyClientSecret(input.getDummyClientSecret());
    oauthFrontendDetails.setOidc(input.getOidc());
    oauthFrontendDetails.setShowDebugInformation(input.getShowDebugInformation());
    oauthFrontendDetails.setRequireHttps(input.getRequireHttps());
    oauthFrontendDetails.setStrictDiscoveryDocumentValidation(
        input.getStrictDiscoveryDocumentValidation());
    oauthFrontendDetails.setUseHttpBasicAuth(input.getUseHttpBasicAuth());

    return oauthFrontendDetails;
  }

  @Override
  public Collection<OAuthFrontendDetails> mapDtosToEntities(
      Collection<OAuthFrontendDetailsDto> input) {
    return input.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
  }

  @Override
  public Collection<OAuthFrontendDetailsDto> mapEntitiesToDtos(
      Collection<OAuthFrontendDetails> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }
}

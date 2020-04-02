package org.burningokr.mapper.configuration;

import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.dto.configuration.OAuthClientDetailsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.springframework.stereotype.Service;

@Service
public class OAuthClientDetailsMapper
    implements DataMapper<OAuthClientDetails, OAuthClientDetailsDto> {
  @Override
  public OAuthClientDetails mapDtoToEntity(OAuthClientDetailsDto input) {
    OAuthClientDetails oauthClientDetails = new OAuthClientDetails();

    oauthClientDetails.setClientId(input.getClientId());
    oauthClientDetails.setClientSecret(input.getClientSecret());
    oauthClientDetails.setWebServerRedirectUri(input.getWebServerRedirectUri());

    oauthClientDetails.setAccessTokenValidity(input.getAccessTokenValidity());
    oauthClientDetails.setRefreshTokenValidity(input.getRefreshTokenValidity());

    return oauthClientDetails;
  }

  @Override
  public OAuthClientDetailsDto mapEntityToDto(OAuthClientDetails input) {
    OAuthClientDetailsDto oauthClientDetailsDto = new OAuthClientDetailsDto();

    oauthClientDetailsDto.setClientId(input.getClientId());
    oauthClientDetailsDto.setClientSecret(input.getClientSecret());
    oauthClientDetailsDto.setWebServerRedirectUri(input.getWebServerRedirectUri());

    oauthClientDetailsDto.setAccessTokenValidity(input.getAccessTokenValidity());
    oauthClientDetailsDto.setRefreshTokenValidity(input.getRefreshTokenValidity());

    return oauthClientDetailsDto;
  }

  @Override
  public Collection<OAuthClientDetails> mapDtosToEntities(Collection<OAuthClientDetailsDto> input) {
    return input.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
  }

  @Override
  public Collection<OAuthClientDetailsDto> mapEntitiesToDtos(Collection<OAuthClientDetails> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }
}

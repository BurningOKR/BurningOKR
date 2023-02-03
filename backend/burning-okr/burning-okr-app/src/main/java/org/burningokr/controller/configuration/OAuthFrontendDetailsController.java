package org.burningokr.controller.configuration;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.configuration.OAuthFrontendDetailsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthConfiguration;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@RestApiController
@RequiredArgsConstructor
public class OAuthFrontendDetailsController {
  private final OAuthConfigurationService oauthConfigurationService;
  private final DataMapper<Collection<OAuthConfiguration>, OAuthFrontendDetailsDto> dataMapper;

  @GetMapping("/oAuthFrontendDetails")
  public ResponseEntity<OAuthFrontendDetailsDto> getOAuthFrontendDetails() {
    Collection<OAuthConfiguration> oAuthConfigurations =
      oauthConfigurationService.getOAuthConfigurations();
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(oAuthConfigurations));
  }
}

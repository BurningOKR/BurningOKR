package org.burningokr.controller.configuration;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.configuration.OAuthFrontendDetailsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthFrontendDetails;
import org.burningokr.service.configuration.OAuthFrontendDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
@RequiredArgsConstructor
public class OAuthFrontendDetailsController {
  private final OAuthFrontendDetailsService oauthFrontendDetailsService;
  private final DataMapper<OAuthFrontendDetails, OAuthFrontendDetailsDto> dataMapper;

  @GetMapping("/oAuthFrontendDetails")
  public ResponseEntity<OAuthFrontendDetailsDto> getOAuthFrontendDetails() {
    OAuthFrontendDetails oauthClientDetails = oauthFrontendDetailsService.getOAuthFrontendDetails();
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(oauthClientDetails));
  }
}

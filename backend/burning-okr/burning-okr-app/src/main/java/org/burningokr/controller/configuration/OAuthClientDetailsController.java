package org.burningokr.controller.configuration;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.configuration.OAuthClientDetailsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.configuration.OAuthClientDetails;
import org.burningokr.service.configuration.OAuthClientDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
@RequiredArgsConstructor
public class OAuthClientDetailsController {
  private final OAuthClientDetailsService oauthClientDetailsService;
  private final DataMapper<OAuthClientDetails, OAuthClientDetailsDto> dataMapper;

  @GetMapping("/oAuthClientDetails")
  public ResponseEntity<OAuthClientDetailsDto> getOauthClientDetails() {
    OAuthClientDetails oauthClientDetails = oauthClientDetailsService.getOAuthClientDetails();
    return ResponseEntity.ok().body(dataMapper.mapEntityToDto(oauthClientDetails));
  }
}

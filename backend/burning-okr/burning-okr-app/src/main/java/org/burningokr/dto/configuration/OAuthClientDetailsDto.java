package org.burningokr.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientDetailsDto {
  @NotNull
  private String clientId;
  @NotNull
  private String clientSecret;
  @NotNull
  private String webServerRedirectUri;

  private Integer accessTokenValidity;
  private Integer refreshTokenValidity;
}

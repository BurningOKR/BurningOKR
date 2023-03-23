package org.burningokr.model.settings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OidcConfiguration {
  private String clientId;
  private String authUrl;
  private String accessTokenUrl;
}

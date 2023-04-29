package org.burningokr.model.settings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OidcConfiguration {
  private String clientId;
  private String issuerUri;
  private List<String> scopes;
  private boolean strictDiscoveryDocumentValidation;
}

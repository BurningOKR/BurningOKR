package org.burningokr.model.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "system.configuration")
@Data
@Component
public class SystemProperties {
  private String apiEndpoint;
  private String oidcAdminGroupName;
  private String clientId;
  private String issuerUri;
  private List<String> scopes;
  private boolean strictDiscoveryDocumentValidation;
  private boolean demoMode = false;
  private String provider;
}

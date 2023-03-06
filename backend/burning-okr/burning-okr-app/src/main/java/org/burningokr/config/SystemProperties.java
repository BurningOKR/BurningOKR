package org.burningokr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "system.configuration")
@Data
public class SystemProperties {
  private String apiEndpoint;
}

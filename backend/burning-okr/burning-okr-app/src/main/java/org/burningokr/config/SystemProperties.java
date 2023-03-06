package org.burningokr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "system.configuration")
@Data
@Component
public class SystemProperties {
  private String apiEndpoint;
}

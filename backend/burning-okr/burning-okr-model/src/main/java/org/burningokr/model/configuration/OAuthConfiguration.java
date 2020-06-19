package org.burningokr.model.configuration;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class OAuthConfiguration {
  @Id private String key;

  private String value;

  private String type;

  public OAuthConfiguration(OAuthConfigurationName name, String value, ConfigurationType type) {
    this.key = name.getName();
    this.value = value;
    this.type = type.getName();
  }
}

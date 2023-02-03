package org.burningokr.model.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class OAuthConfiguration {
  @Id
  private String key;

  private String value;

  private String type;

  public OAuthConfiguration(OAuthConfigurationName name, String value, ConfigurationType type) {
    this.key = name.getName();
    this.value = value;
    this.type = type.getName();
  }
}

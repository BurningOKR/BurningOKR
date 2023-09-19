package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    return new JwtAuthenticationConverter();
  }
}

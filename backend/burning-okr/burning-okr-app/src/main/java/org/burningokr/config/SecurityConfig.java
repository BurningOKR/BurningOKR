package org.burningokr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${system.configuration.api-endpoint}")
  private String apiPrefix;

  public void configure(HttpSecurity http) throws Exception{
    http
      .authorizeRequests()
      .antMatchers(patterns())
      .permitAll()
      .and()
      .authorizeRequests()
      .anyRequest()
      .authenticated()
//      .and()
//      .oauth2ResourceServer()
//      .jwt()
//      .jwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter())
      ;
  }

  private String[] patterns() {
    return new String[] {
      "/v2/api-docs",
      "/swagger-resources/configuration/ui",
      "/swagger-resources/configuration/security",
      "/csrf",
      "/swagger-resources",
      "/swagger-ui.html**",
      "/webjars/**",
      apiPrefix + "/init/**",
      apiPrefix + "/local-users/password",
      apiPrefix + "/local-users/forgot-password",
      apiPrefix + "/oAuthFrontendDetails",
      "/wsregistry"
    };
  }

  @Bean
  public JwtDecoder jwtDecoder(){
    return NimbusJwtDecoder
      .withSecretKey(new SecretKeySpec("SomeSecretWeDontCheckItAnywayToS".getBytes(), "HmacSHA256"))
      .build();
  }
}

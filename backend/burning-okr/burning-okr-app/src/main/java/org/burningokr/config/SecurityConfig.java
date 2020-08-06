package org.burningokr.config;

import org.burningokr.service.environment.AppEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${system.configuration.api-endpoint}")
  private String apiPrefix;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(patterns());
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
      apiPrefix + "/oAuthFrontendDetails"
    };
  }
}

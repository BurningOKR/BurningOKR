package org.burningokr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
      "/api/init/**",
      "/api/local-users/password",
      "/api/local-users/forgot-password",
      "/api/oAuthFrontendDetails"
    };
  }
}

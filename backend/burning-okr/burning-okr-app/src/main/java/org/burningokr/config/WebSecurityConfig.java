package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.SystemProperties;
import org.burningokr.service.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final CustomAuthenticationProvider customAuthenticationProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
    http.cors().configurationSource(corsConfigurationSource());

    // session policy
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.csrf().disable(); // TODO check if needed

    http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
      response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"");
      response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    });

    setUnauthorizedUriRoutes(http);

    http.oauth2ResourceServer().jwt();

    http.authenticationManager(authManager);

    return http.build();
  }

  private void setUnauthorizedUriRoutes(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests()
      .requestMatchers(
        "/v2/api-docs/**",
        "/swagger-resources/configuration/ui",
        "/swagger-resources/configuration/security",
        "/swagger-resources",
        "/swagger-ui.html**",
        "/webjars/**",
        "/wsregistry",
        "/actuator/**",
        "/applicationSettings/oidcConfiguration" // TODO try to move to /api
      )
      .permitAll()
      .anyRequest()
      .authenticated();
  }

  private CorsConfigurationSource corsConfigurationSource() {
    final var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("*"));

    // Limited to API routes (neither actuator nor Swagger-UI)
    final var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);

    return source;
  }

  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    var authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    return authBuilder.authenticationProvider(customAuthenticationProvider).build();
  }
}

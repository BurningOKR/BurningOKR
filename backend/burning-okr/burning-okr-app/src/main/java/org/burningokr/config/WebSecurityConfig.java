package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

  private final SystemProperties systemProperties;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().configurationSource(corsConfigurationSource());

    // session policy
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.csrf().disable(); // TODO check if needed

    http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
      response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"");
      response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    });

    http
      .authorizeHttpRequests()
      .requestMatchers(
        "/v2/api-docs/**",
        "/swagger-resources/configuration/ui",
        "/swagger-resources/configuration/security",
        "/swagger-resources",
        "/swagger-ui.html**",
        "/webjars/**",
//        systemProperties.getApiEndpoint() + "/oAuthFrontendDetails",
        "/wsregistry",
        "/actuator/**",
        systemProperties.getApiEndpoint() + "/companies/**" // TODO open for testing-purposes
      )
      .permitAll()
      .anyRequest()
      .authenticated();

    http
      .oauth2ResourceServer()
      .jwt();

    return http.build();
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
}

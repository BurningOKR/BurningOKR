package org.burningokr.config.swagger;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthFrontendDetails;
import org.burningokr.service.condition.LocalUserCondition;
import org.burningokr.service.configuration.OAuthFrontendDetailsService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Arrays;
import java.util.Collections;

@Conditional(LocalUserCondition.class)
@EnableAutoConfiguration
@Configuration
@RequiredArgsConstructor
public class SwaggerLocalConfig extends SwaggerConfig {

  private final OAuthFrontendDetailsService oAuthFrontendDetailsService;

  @Override
  @Bean
  public Docket api() {
    return baseApi()
        .securitySchemes(Collections.singletonList(securityScheme()))
        .securityContexts(Collections.singletonList(securityContext()));
  }

  @Override
  @Bean
  public SecurityConfiguration security() {
    OAuthFrontendDetails oAuthFrontendDetails = oAuthFrontendDetailsService.getOAuthFrontendDetails();

    return SecurityConfigurationBuilder.builder()
        .clientId(oAuthFrontendDetails.getClientId())
        .clientSecret(oAuthFrontendDetails.getDummyClientSecret())
        .build();
  }

  private SecurityScheme securityScheme() {
    GrantType passwordGrant = new ResourceOwnerPasswordCredentialsGrant("/oauth/token");

    return new OAuthBuilder()
        .name("spring_oauth")
        .grantTypes(Collections.singletonList(passwordGrant))
        .scopes(Arrays.asList(scopes()))
        .build();
  }

  private AuthorizationScope[] scopes() {
    return new AuthorizationScope[]{
        new AuthorizationScope("USER", "user")
    };
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(
            Collections.singletonList(new SecurityReference("spring_oauth", scopes())))
        .forPaths(PathSelectors.any())
        .build();
  }
}

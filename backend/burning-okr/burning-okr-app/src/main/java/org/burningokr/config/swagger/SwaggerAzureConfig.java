package org.burningokr.config.swagger;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.userutil.ExternalOAuthClientDetails;
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

@Conditional(AadCondition.class)
@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
public class SwaggerAzureConfig extends SwaggerConfig {

  private final ExternalOAuthClientDetails externalOAuthClientDetails;

  @Override
  @Bean
  public Docket api() {
    return baseApi()
        .securitySchemes(Collections.singletonList(securityScheme()))
        .securityContexts(Collections.singletonList(securityContext()));
  }

  @Bean
  public SecurityConfiguration security() {

    Map<String, Object> queryParams = new HashMap<>();

    return SecurityConfigurationBuilder.builder()
        .clientId(externalOAuthClientDetails.getClientId())
        .scopeSeparator(",")
        .useBasicAuthenticationWithAccessCodeGrant(true)
        .additionalQueryStringParams(queryParams)
        .build();
  }

  private SecurityScheme securityScheme() {
    ImplicitGrant implicitGrant =
        new ImplicitGrant(
            new LoginEndpoint(externalOAuthClientDetails.getUserAuthorizationUri()),
            externalOAuthClientDetails.getTokenName());

    return new OAuthBuilder()
        .name("spring_oauth")
        .grantTypes(Collections.singletonList(implicitGrant))
        .scopes(Arrays.asList(scopes()))
        .build();
  }

  private AuthorizationScope[] scopes() {
    return new AuthorizationScope[] {
      new AuthorizationScope(externalOAuthClientDetails.getScope(), "")
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

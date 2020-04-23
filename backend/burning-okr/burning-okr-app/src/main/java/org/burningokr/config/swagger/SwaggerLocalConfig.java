package org.burningokr.config.swagger;

import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.burningokr.service.condition.LocalUserCondition;
import org.burningokr.service.configuration.OAuthConfigurationService;
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

@Conditional(LocalUserCondition.class)
@EnableAutoConfiguration
@Configuration
@RequiredArgsConstructor
public class SwaggerLocalConfig extends SwaggerConfig {

  private final OAuthConfigurationService oAuthConfigurationService;

  @Override
  @Bean
  public Docket api() {
    return baseApi()
        .securitySchemes(Collections.singletonList(securityScheme()))
        .securityContexts(Collections.singletonList(securityContext()));
  }

  @Override
  public SecurityConfiguration security() {
    return null;
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
    return new AuthorizationScope[] {new AuthorizationScope("USER", "user")};
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(
            Collections.singletonList(new SecurityReference("spring_oauth", scopes())))
        .forPaths(PathSelectors.any())
        .build();
  }
}

package org.burningokr.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.burningokr.properties.AuthentificationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

  private final AuthentificationProperties authentificationProperties;

  @Autowired
  public SwaggerConfig(
      @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
          AuthentificationProperties authentificationProperties) {
    this.authentificationProperties = authentificationProperties;
  }

  /**
   * Create and configure Docket Bean for Swagger.
   *
   * @return a {@link Docket} object
   */
  @Bean
  public Docket api() {

    List<ResponseMessage> list = new ArrayList<>();
    list.add(
        new ResponseMessageBuilder()
            .code(500)
            .message("500 message")
            .responseModel(new ModelRef("Result"))
            .build());
    list.add(
        new ResponseMessageBuilder()
            .code(401)
            .message("Unauthorized")
            .responseModel(new ModelRef("Result"))
            .build());
    list.add(
        new ResponseMessageBuilder()
            .code(406)
            .message("Not Acceptable")
            .responseModel(new ModelRef("Result"))
            .build());

    return new Docket(DocumentationType.SWAGGER_2)
        .globalResponseMessage(RequestMethod.POST, list)
        .select()
        .apis(RequestHandlerSelectors.basePackage("org.burningokr.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .securitySchemes(Collections.singletonList(securityScheme()))
        .securityContexts(Collections.singletonList(securityContext()));
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "OKR Tool REST API",
        "Dokumentation der REST Features des OKR Tools",
        "v.0.0.1-SNAPSHOT",
        "terms.of.service.url",
        new Contact("Department of API-Design", "my.url", "contact@api.invalid"),
        "No licencing yet",
        "no licensing url yet",
        Collections.emptyList());
  }

  /**
   * Create and configure SecurityConfiguration Bean for Swagger.
   *
   * @return a {@link SecurityConfiguration} object
   */
  @Bean
  public SecurityConfiguration security() {

    Map<String, Object> queryParams = new HashMap<>();

    return SecurityConfigurationBuilder.builder()
        .clientId(authentificationProperties.getClientId())
        .scopeSeparator(",")
        .useBasicAuthenticationWithAccessCodeGrant(true)
        .additionalQueryStringParams(queryParams)
        .build();
  }

  private SecurityScheme securityScheme() {
    ImplicitGrant implicitGrant =
        new ImplicitGrant(
            new LoginEndpoint(authentificationProperties.getUserAuthorizationUri()),
            authentificationProperties.getTokenName());

    return new OAuthBuilder()
        .name("spring_oauth")
        .grantTypes(Collections.singletonList(implicitGrant))
        .scopes(Arrays.asList(scopes()))
        .build();
  }

  private String generateNonce(int length) {
    StringBuilder nonceStringBuilder = new StringBuilder();

    char[] characters =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    Random random = new Random();

    for (int i = 0; i < length; i++) {
      nonceStringBuilder.append(characters[random.nextInt(characters.length)]);
    }

    return nonceStringBuilder.toString();
  }

  private AuthorizationScope[] scopes() {
    return new AuthorizationScope[] {
      new AuthorizationScope(authentificationProperties.getScope(), "")
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

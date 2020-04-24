package org.burningokr.config.swagger;

import java.util.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Order(50)
@EnableAutoConfiguration
public abstract class SwaggerConfig {

  /**
   * Create and configure Docket Bean for Swagger.
   *
   * @return a {@link Docket} object
   */
  public abstract Docket api();

  /**
   * Create and configure SecurityConfiguration Bean for Swagger.
   *
   * @return a {@link SecurityConfiguration} object
   */
  public abstract SecurityConfiguration security();

  /**
   * Create and configure Docket Bean for Swagger. This is the base docket, which can be extended by
   * the child classes
   *
   * @return a {@link Docket} object
   */
  protected Docket baseApi() {

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
        .apiInfo(apiInfo());
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
}

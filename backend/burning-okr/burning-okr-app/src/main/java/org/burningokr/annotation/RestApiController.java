package org.burningokr.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RestController
@RequestMapping
public @interface RestApiController {
  /**
   * The value of the RestApiController, default "/api".
   *
   * @return a string value
   */
  @AliasFor(annotation = RequestMapping.class, attribute = "value")
  String[] value() default "${system.configuration.api-endpoint}";
}

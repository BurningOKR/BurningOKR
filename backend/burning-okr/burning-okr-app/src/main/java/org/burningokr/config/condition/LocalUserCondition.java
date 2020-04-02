package org.burningokr.config.condition;

import org.burningokr.service.EnvironmentService;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LocalUserCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    boolean isLocalUserCondition =
        context
            .getEnvironment()
            .getProperty(EnvironmentService.authMode)
            .equals(EnvironmentService.authModeLocal);
    return isLocalUserCondition;
  }
}

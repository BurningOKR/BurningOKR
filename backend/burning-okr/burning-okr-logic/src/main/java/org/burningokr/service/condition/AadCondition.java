package org.burningokr.service.condition;

import org.burningokr.service.EnvironmentService;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class AadCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    boolean isAadCondition =
        context
            .getEnvironment()
            .getProperty(EnvironmentService.authMode)
            .equals(EnvironmentService.authModeAad);
    return isAadCondition;
  }
}

package org.burningokr.service.condition;

import org.burningokr.service.environment.AuthModes;
import org.burningokr.service.environment.EnvironmentPropertyNames;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class AadCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    boolean isAadCondition =
        context
            .getEnvironment()
            .getProperty(EnvironmentPropertyNames.AUTH_MODE.getFullName())
            .equals(AuthModes.AZURE.getName());
    return isAadCondition;
  }
}

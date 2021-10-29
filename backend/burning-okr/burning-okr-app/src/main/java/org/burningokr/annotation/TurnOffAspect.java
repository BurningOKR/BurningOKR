package org.burningokr.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.burningokr.service.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@ConditionalOnExpression("${spring.environment.demo}")
@Aspect
@Component
public class TurnOffAspect {

  @Pointcut("@annotation(org.burningokr.annotation.TurnOff)")
  private void turnOffAnnotation() {}

  @Around("org.burningokr.annotation.TurnOffAspect.turnOffAnnotation()")
  public Object turnOff(ProceedingJoinPoint pjp) throws ForbiddenException {
    throw new ForbiddenException("Method disabled");
  }

}

package org.burningokr.service.condition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AadConditionTest {

  private AadCondition aadCondition;

  private AnnotatedTypeMetadata annotatedTypeMetadata;

  private ConditionContext conditionContext;

  @BeforeEach
  public void setup() {
    this.aadCondition = new AadCondition();
    annotatedTypeMetadata = mock(AnnotatedTypeMetadata.class);
    conditionContext = mock(ConditionContext.class);
  }

  @Test
  public void test_auth_mode_random_string_matches_should_be_false() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("system.configuration.auth-mode", "123412531235");
    BDDMockito.given(conditionContext.getEnvironment()).willReturn(environment);
    assertFalse(aadCondition.matches(conditionContext, annotatedTypeMetadata));
  }

  @Test
  public void test_auth_mode_aad_matches_should_be_true() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("system.configuration.auth-mode", "azure");
    BDDMockito.given(conditionContext.getEnvironment()).willReturn(environment);
    assertTrue(aadCondition.matches(conditionContext, annotatedTypeMetadata));
  }

  @Test
  public void test_auth_mode_local_matches_should_be_false() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("system.configuration.auth-mode", "local");
    BDDMockito.given(conditionContext.getEnvironment()).willReturn(environment);
    assertFalse(aadCondition.matches(conditionContext, annotatedTypeMetadata));
  }
}

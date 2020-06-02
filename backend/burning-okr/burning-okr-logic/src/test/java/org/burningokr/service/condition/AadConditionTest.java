package org.burningokr.service.condition;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mock.env.MockEnvironment;

@RunWith(MockitoJUnitRunner.class)
public class AadConditionTest {

  private AadCondition aadCondition;

  private AnnotatedTypeMetadata annotatedTypeMetadata;

  private ConditionContext conditionContext;

  @Before
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
    Assert.assertFalse(aadCondition.matches(conditionContext, annotatedTypeMetadata));
  }

  @Test
  public void test_auth_mode_aad_matches_should_be_true() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("system.configuration.auth-mode", "aad");
    BDDMockito.given(conditionContext.getEnvironment()).willReturn(environment);
    Assert.assertTrue(aadCondition.matches(conditionContext, annotatedTypeMetadata));
  }

  @Test
  public void test_auth_mode_local_matches_should_be_false() {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("system.configuration.auth-mode", "local");
    BDDMockito.given(conditionContext.getEnvironment()).willReturn(environment);
    Assert.assertFalse(aadCondition.matches(conditionContext, annotatedTypeMetadata));
  }
}

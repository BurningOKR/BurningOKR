package org.burningokr.config;

import org.burningokr.config.condition.AadCondition;
import org.burningokr.config.condition.LocalUserCondition;
import org.burningokr.service.initialisation.AadInitOrderService;
import org.burningokr.service.initialisation.InitOrderService;
import org.burningokr.service.initialisation.LocalInitOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitOrderServiceConfig {

  @Bean
  @Conditional(LocalUserCondition.class)
  public InitOrderService getLocalInitOrderService() {
    return new LocalInitOrderService();
  }

  @Bean
  @Conditional(AadCondition.class)
  public InitOrderService getAadInitOrderService() {
    return new AadInitOrderService();
  }
}

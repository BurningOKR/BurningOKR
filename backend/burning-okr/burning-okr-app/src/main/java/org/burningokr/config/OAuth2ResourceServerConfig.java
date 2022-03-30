//package org.burningokr.config;
//
//import org.burningokr.service.condition.LocalUserCondition;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//
//@Conditional(LocalUserCondition.class)
//@Configuration
//public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
//  private final DefaultTokenServices defaultTokenServices;
//
//  @Autowired
//  public OAuth2ResourceServerConfig(DefaultTokenServices defaultTokenServices) {
//
//    this.defaultTokenServices = defaultTokenServices;
//  }
//
//  @Override
//  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//    resources.tokenServices(defaultTokenServices);
//  }
//}

//package org.burningokr.config.authorizationserver;
//
//import lombok.RequiredArgsConstructor;
//import org.burningokr.repositories.users.LocalUserRepository;
//import org.burningokr.service.LocalUserDetailsService;
//import org.burningokr.service.condition.LocalUserCondition;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@Conditional(LocalUserCondition.class)
//@EnableWebSecurity
//@Order(20)
//@RequiredArgsConstructor
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//  private final LocalUserRepository localUserRepository;
//
//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
//
//  @Bean
//  @Override
//  protected UserDetailsService userDetailsService() {
//    return new LocalUserDetailsService(localUserRepository);
//  }
//}

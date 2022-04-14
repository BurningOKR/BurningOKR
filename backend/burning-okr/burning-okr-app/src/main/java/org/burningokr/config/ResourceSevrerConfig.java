//package org.burningokr.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//public class ResourceSevrerConfig {
//
//  @Bean
//  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
////      .authorizeRequests()
////      .antMatchers(patterns())
////      .permitAll()
////      .and()
//      .authorizeRequests()
//      .anyRequest()
////      .hasRole("USER")
////      .authenticated()
//      .permitAll()
//      .and()
////      .oauth2ResourceServer()
////      .jwt()
////      .decoder(jwtDecoder())
////      .jwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter())
//    ;
//    return http.build();
//  }
//}

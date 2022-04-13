package org.burningokr.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

@EnableWebSecurity
public class DefaultSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated().and().formLogin(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  UserDetailsService users() {
    UserDetails user = User
      .withUsername("user1@aol.com")
      .passwordEncoder(passwordEncoder()::encode)
      .password("password")
      .roles("USER")
      .build();
    UserDetails user2 = User
      .withUsername("Timok@brockhaus-ag.de")
      .passwordEncoder(passwordEncoder()::encode)
      .password("123456789")
      .roles("USER")
      .build();

    ArrayList<UserDetails> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user2);
    return new InMemoryUserDetailsManager(userList);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

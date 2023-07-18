package org.burningokr.service.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO (F. L 28.06.2023) add tests
@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

//  private CustomAuthenticationProvider customAuthenticationProvider;
//  private Class<?> aauthentication;
//  @Mock
//  private Authentication authentication;
//  @Mock
//  private JwtAuthenticationProvider jwtAuthenticationProvider;
//  @Mock
//  private JwtDecoder jwtDecoder;
//  @Mock
//  private AuthorizationUserContextService userContextService;
//  @Mock
//  private AbstractAuthenticationToken token;
//
//  @BeforeEach
//  public void setUp() {
//    customAuthenticationProvider = new CustomAuthenticationProvider(jwtDecoder, userContextService);
//    authentication.setAuthenticated(false);
//  }
//
//  @Test
//  public void authenticate_should() {
//    when(jwtAuthenticationProvider.authenticate(authentication)).thenReturn(token);
//    when(token.isAuthenticated()).thenReturn(false);
//
//    assertEquals(token, customAuthenticationProvider.authenticate(authentication));
//
//  }
//
//  @Test
//  public void supports_should() {
//    assertTrue(customAuthenticationProvider.supports(aauthentication));
//
//  }


}

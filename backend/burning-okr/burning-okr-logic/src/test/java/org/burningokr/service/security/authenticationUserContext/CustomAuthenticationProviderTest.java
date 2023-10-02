package org.burningokr.service.security.authenticationUserContext;

import org.burningokr.model.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

  @InjectMocks
  CustomAuthenticationProvider customAuthenticationProvider;

  @Mock
  AuthenticationUserContextService authenticationUserContextServiceMock;
  @Mock
  JwtDecoder jwtDecoderMock;


  @Test
  void authenticate_shouldReturnBurningOkrAuthentication() {
    //assemble
    Authentication authenticationMock = mock(Authentication.class);

    //mock constructor of JwtAuthenticationProvider
    try (MockedConstruction<JwtAuthenticationProvider> ignored =
           mockConstruction(
             JwtAuthenticationProvider.class,
             (mock, context) -> when(mock.authenticate(authenticationMock)).thenReturn(authenticationMock))) {

      Jwt jwtMock = mock(Jwt.class);
      doReturn(jwtMock).when(authenticationMock).getCredentials();
      User userMock = mock(User.class);
      doReturn(userMock).when(this.authenticationUserContextServiceMock).getUserFromToken(jwtMock);
      doReturn(false).when(authenticationMock).isAuthenticated();

      //act
      Authentication result = this.customAuthenticationProvider.authenticate(authenticationMock);

      //assert
      Assertions.assertInstanceOf(BurningOkrAuthentication.class, result);

      BurningOkrAuthentication bokrResult = (BurningOkrAuthentication) result;
      Assertions.assertSame(jwtMock, bokrResult.getToken());
      Assertions.assertSame(userMock, bokrResult.getPrincipal());
      assertFalse(bokrResult.isAuthenticated());
      verifyNoMoreInteractions(this.authenticationUserContextServiceMock);
    }
  }

  @Test
  void authenticate_shouldReturnBurningOkrAuthenticationAndUpdateCachedAndDatabaseUser() {
    //assemble
    Authentication authenticationMock = mock(Authentication.class);

    //mock constructor of JwtAuthenticationProvider
    try (MockedConstruction<JwtAuthenticationProvider> ignored =
           mockConstruction(
             JwtAuthenticationProvider.class,
             (mock, context) -> when(mock.authenticate(authenticationMock)).thenReturn(authenticationMock))) {

      Jwt jwtMock = mock(Jwt.class);
      doReturn(jwtMock).when(authenticationMock).getCredentials();
      User userMock = mock(User.class);
      doReturn(userMock).when(this.authenticationUserContextServiceMock).getUserFromToken(jwtMock);
      doReturn(true).when(authenticationMock).isAuthenticated();
      doNothing().when(this.authenticationUserContextServiceMock).updateCachedAndDatabaseUser(userMock);

      //act
      Authentication result = this.customAuthenticationProvider.authenticate(authenticationMock);

      //assert
      Assertions.assertInstanceOf(BurningOkrAuthentication.class, result);

      BurningOkrAuthentication bokrResult = (BurningOkrAuthentication) result;
      Assertions.assertSame(jwtMock, bokrResult.getToken());
      Assertions.assertSame(userMock, bokrResult.getPrincipal());
      assertTrue(bokrResult.isAuthenticated());
      verify(this.authenticationUserContextServiceMock).updateCachedAndDatabaseUser(userMock);
      verifyNoMoreInteractions(this.authenticationUserContextServiceMock);
    }
  }

  @Test
  void authenticate_shouldThrowAuthenticationException() {
    //assemble
    Authentication authenticationMock = mock(Authentication.class);

    //mock constructor of JwtAuthenticationProvider
    try (MockedConstruction<JwtAuthenticationProvider> ignored =
           mockConstruction(
             JwtAuthenticationProvider.class,
             (mock, context) -> doThrow(InvalidBearerTokenException.class).when(mock).authenticate(authenticationMock))) {

      //act + assert
      Assertions.assertThrows(
        AuthenticationException.class,
        () -> this.customAuthenticationProvider.authenticate(authenticationMock));
    }
  }

  @Test
  void supports_shouldReturnTrue() {
    //assemble
    Class<BearerTokenAuthenticationToken> tokenClass = BearerTokenAuthenticationToken.class;

    //act
    boolean result = this.customAuthenticationProvider.supports(tokenClass);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void supports_shouldReturnTrue_whenClassIsSubclassOfBearerTokenAuthenticationToken() {
    //assemble
    Class<BearerTokenAuthenticationTokenTestImpl> tokenClass = BearerTokenAuthenticationTokenTestImpl.class;

    //act
    boolean result = this.customAuthenticationProvider.supports(tokenClass);

    //assert
    Assertions.assertTrue(result);
  }

  @Test
  void supports_shouldReturnFalse() {
    //assemble
    Class<Object> objectTokenClass = Object.class;

    //act
    boolean result = this.customAuthenticationProvider.supports(objectTokenClass);

    //assert
    Assertions.assertFalse(result);
  }

  private static class BearerTokenAuthenticationTokenTestImpl extends BearerTokenAuthenticationToken {

    public BearerTokenAuthenticationTokenTestImpl(String token) {
      super(token);
    }
  }
}

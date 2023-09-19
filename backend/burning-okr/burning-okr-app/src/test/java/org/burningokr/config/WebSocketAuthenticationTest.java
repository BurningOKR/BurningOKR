package org.burningokr.config;

import org.burningokr.exceptions.AuthorizationHeaderException;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.hibernate.annotations.OnDelete;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketAuthenticationTest {

  @InjectMocks
  WebSocketAuthentication webSocketAuthentication;

  @Mock
  JwtDecoder jwtDecoderMock;

  @Mock
  JwtAuthenticationConverter jwtAuthenticationConverterMock;

  @Mock
  AuthenticationUserContextService authenticationUserContextServiceMock;



  @Test
  void isConnectionAttempt_shouldReturnTrue() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(StompCommand.CONNECT).when(stompHeaderAccessorMock).getCommand();

    //act
    boolean result = this.webSocketAuthentication.isConnectionAttempt(stompHeaderAccessorMock);

    //assert
    Assertions.assertTrue(result);
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void isConnectionAttempt_shouldReturnFalse() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(StompCommand.MESSAGE).when(stompHeaderAccessorMock).getCommand();

    //act
    boolean result = this.webSocketAuthentication.isConnectionAttempt(stompHeaderAccessorMock);

    //assert
    Assertions.assertFalse(result);
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void tryToAuthenticate_shouldThrowNullPointerException_whenStompHeaderHasNoNativeHeader() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(false).when(stompHeaderAccessorMock).containsNativeHeader("Authorization");

    //act + assert
    NullPointerException exc = Assertions.assertThrows(NullPointerException.class, () ->
      this.webSocketAuthentication.tryToAuthenticate(stompHeaderAccessorMock));

    //assert
    Assertions.assertEquals("Token is empty", exc.getMessage());
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void tryToAuthenticate_shouldThrowNullPointerException_whenHeaderIsNull() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(true).when(stompHeaderAccessorMock).containsNativeHeader("Authorization");
    doReturn(null).when(stompHeaderAccessorMock).getNativeHeader("Authorization");

    //act + assert
    NullPointerException exc = Assertions.assertThrows(NullPointerException.class, () ->
      this.webSocketAuthentication.tryToAuthenticate(stompHeaderAccessorMock));

    //assert
    Assertions.assertEquals("Token is empty", exc.getMessage());
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void tryToAuthenticate_shouldThrowNullPointerException_whenHeaderIsEmpty() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(true).when(stompHeaderAccessorMock).containsNativeHeader("Authorization");
    doReturn(List.of()).when(stompHeaderAccessorMock).getNativeHeader("Authorization");

    //act + assert
    NullPointerException exc = Assertions.assertThrows(NullPointerException.class, () ->
      this.webSocketAuthentication.tryToAuthenticate(stompHeaderAccessorMock));

    //assert
    Assertions.assertEquals("Token is empty", exc.getMessage());
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void tryToAuthenticate_shouldThrowAuthorizationHeaderException_whenIsNotAuthenticated() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(true).when(stompHeaderAccessorMock).containsNativeHeader("Authorization");
    doReturn(List.of("Bearer dummyBearerTokenString")).when(stompHeaderAccessorMock).getNativeHeader("Authorization");

    Jwt jwtMock = mock(Jwt.class);
    doReturn(jwtMock).when(this.jwtDecoderMock).decode("dummyBearerTokenString");

    AbstractAuthenticationToken abstractAuthenticationTokenMock = mock(AbstractOAuth2TokenAuthenticationToken.class);
    doReturn(false).when(abstractAuthenticationTokenMock).isAuthenticated();
    doReturn(abstractAuthenticationTokenMock).when(this.jwtAuthenticationConverterMock).convert(jwtMock);

    User userMock = mock(User.class);
    doReturn(userMock).when(this.authenticationUserContextServiceMock).getUserFromToken(jwtMock);

    //act + assert
    AuthorizationHeaderException exc = Assertions.assertThrows(AuthorizationHeaderException.class, () ->
      this.webSocketAuthentication.tryToAuthenticate(stompHeaderAccessorMock));

    //assert
    Assertions.assertEquals("User is not authenticated", exc.getMessage());
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void tryToAuthenticate_shouldThrowRuntimeException_whenHeaderSessionAttributesAreNull() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(true).when(stompHeaderAccessorMock).containsNativeHeader("Authorization");
    doReturn(List.of("Bearer dummyBearerTokenString")).when(stompHeaderAccessorMock).getNativeHeader("Authorization");

    Jwt jwtMock = mock(Jwt.class);
    doReturn(jwtMock).when(this.jwtDecoderMock).decode("dummyBearerTokenString");

    AbstractAuthenticationToken abstractAuthenticationTokenMock = mock(AbstractOAuth2TokenAuthenticationToken.class);
    doReturn(true).when(abstractAuthenticationTokenMock).isAuthenticated();
    doReturn(abstractAuthenticationTokenMock).when(this.jwtAuthenticationConverterMock).convert(jwtMock);

    User userMock = mock(User.class);
    doReturn(userMock).when(this.authenticationUserContextServiceMock).getUserFromToken(jwtMock);

    doNothing().when(this.authenticationUserContextServiceMock).updateCachedAndDatabaseUser(userMock);

    doReturn(null).when(stompHeaderAccessorMock).getSessionAttributes();

    //act + assert
    RuntimeException exc = Assertions.assertThrows(RuntimeException.class, () ->
      this.webSocketAuthentication.tryToAuthenticate(stompHeaderAccessorMock));

    //assert
    Assertions.assertEquals("Session attributes are null", exc.getMessage());
    verify(this.authenticationUserContextServiceMock).updateCachedAndDatabaseUser(userMock);
    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }

  @Test
  void tryToAuthenticate_shouldSetCorrectHeaderAttributes() {
    //assemble
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
    doReturn(true).when(stompHeaderAccessorMock).containsNativeHeader("Authorization");
    doReturn(List.of("Bearer dummyBearerTokenString")).when(stompHeaderAccessorMock).getNativeHeader("Authorization");

    Jwt jwtMock = mock(Jwt.class);
    doReturn(jwtMock).when(this.jwtDecoderMock).decode("dummyBearerTokenString");

    AbstractAuthenticationToken abstractAuthenticationTokenMock = mock(AbstractOAuth2TokenAuthenticationToken.class);
    doReturn(true).when(abstractAuthenticationTokenMock).isAuthenticated();
    doReturn(abstractAuthenticationTokenMock).when(this.jwtAuthenticationConverterMock).convert(jwtMock);

    User userMock = mock(User.class);
    UUID userID = UUID.randomUUID();
    doReturn(userMock).when(this.authenticationUserContextServiceMock).getUserFromToken(jwtMock);
    doReturn(userID).when(userMock).getId();

    doNothing().when(this.authenticationUserContextServiceMock).updateCachedAndDatabaseUser(userMock);

    Map<String, Object> headerSessionAttributesMap = new HashMap<>();
    doReturn(headerSessionAttributesMap).when(stompHeaderAccessorMock).getSessionAttributes();

    doReturn(userMock).when(this.authenticationUserContextServiceMock).getAuthenticatedUser();

    //act
    this.webSocketAuthentication.tryToAuthenticate(stompHeaderAccessorMock);

    //assert
    verify(this.authenticationUserContextServiceMock).updateCachedAndDatabaseUser(userMock);

    ArgumentCaptor<Authentication> authenticationArgumentCaptor = ArgumentCaptor.forClass(Authentication.class);
    verify(stompHeaderAccessorMock).setUser(authenticationArgumentCaptor.capture());
    Assertions.assertTrue(authenticationArgumentCaptor.getValue().isAuthenticated());
    Assertions.assertSame(userMock, authenticationArgumentCaptor.getValue().getPrincipal());
    Assertions.assertEquals(userID, headerSessionAttributesMap.get("userId"));

    verifyNoMoreInteractions(this.authenticationUserContextServiceMock, this.jwtAuthenticationConverterMock);
  }
}

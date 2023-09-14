package org.burningokr.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StompHeaderInterceptorTest {

  @InjectMocks
  private StompHeaderInterceptor interceptor;

  @Mock
  private WebSocketAuthentication wsAuth;

  @Test
  void preSend_shouldThrowNullPointerException() {
    try (MockedStatic<MessageHeaderAccessor> msgHeaderAccessorMock = mockStatic(MessageHeaderAccessor.class)) {

      //assemble
      msgHeaderAccessorMock.when(() -> MessageHeaderAccessor.getAccessor(any())).thenReturn(null);
      Message message = mock(Message.class);
      MessageChannel messageChannel = mock(MessageChannel.class);

      //act + assert
      Assertions.assertThrows(NullPointerException.class,
        () -> this.interceptor.preSend(message, messageChannel));
    }
  }

  @Test
  void preSend_shouldCallTryAuthenticateAndReturnMessage() {
    try (MockedStatic<MessageHeaderAccessor> msgHeaderAccessorMock = mockStatic(MessageHeaderAccessor.class)) {

      //assemble
      Message message = mock(Message.class);
      MessageChannel messageChannel = mock(MessageChannel.class);

      StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
      msgHeaderAccessorMock.when(() -> MessageHeaderAccessor.getAccessor(any(Message.class), any())).thenReturn(stompHeaderAccessorMock);

      doReturn(true).when(this.wsAuth).isConnectionAttempt(stompHeaderAccessorMock);
      doNothing().when(this.wsAuth).tryToAuthenticate(stompHeaderAccessorMock);

      //act
      Message<?> result = this.interceptor.preSend(message, messageChannel);

      // assert
      verify(this.wsAuth).isConnectionAttempt(stompHeaderAccessorMock);
      verify(this.wsAuth).tryToAuthenticate(stompHeaderAccessorMock);
      assertSame(message, result);
    }
  }

  @Test
  void preSend_shouldNotCallTryAuthenticateAndReturnMessage() {
    try (MockedStatic<MessageHeaderAccessor> msgHeaderAccessorMock = mockStatic(MessageHeaderAccessor.class)) {

      //assemble
      Message message = mock(Message.class);
      MessageChannel messageChannel = mock(MessageChannel.class);

      StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
      msgHeaderAccessorMock.when(() -> MessageHeaderAccessor.getAccessor(any(Message.class), any())).thenReturn(stompHeaderAccessorMock);

      doReturn(false).when(this.wsAuth).isConnectionAttempt(stompHeaderAccessorMock);

      //act
      Message<?> result = this.interceptor.preSend(message, messageChannel);

      // assert
      verify(this.wsAuth).isConnectionAttempt(stompHeaderAccessorMock);
      assertSame(message, result);
    }
  }
}
